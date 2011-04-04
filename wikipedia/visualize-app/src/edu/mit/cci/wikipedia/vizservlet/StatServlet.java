package edu.mit.cci.wikipedia.vizservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mit.cci.wikipedia.collector.GetRevisions;
import edu.mit.cci.wikipedia.util.TimeWindowCount;

@SuppressWarnings("serial")
public class StatServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(StatServlet.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		PrintWriter out = null;
		String output = "";
		String eol = "\n";
		try {
			
			request.setCharacterEncoding("UTF-8");
			// Get parameters
			String pageTitle = request.getParameter("name"); // WikiPedia article title
			if (pageTitle != null) {
				String lang = "en";
				if (request.getParameter("lang") != null) {
					lang = request.getParameter("lang");
				}
				
				PersistenceManager pm = PMF.get().getPersistenceManager();
				String query = "select from " + ArticleCache.class.getName() + " where pageTitle==\'" + pageTitle.replaceAll("\'", "\\\\\'") + "\' order by date";
				List<ArticleCache> articleCaches = (List<ArticleCache>)pm.newQuery(query).execute();
				String data = "";
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (articleCaches.isEmpty()) {
					log.info("No cached");
					
					// Get # of edits on the pageTitle
					GetRevisions gr = new GetRevisions();
					String download = gr.getArticleRevisions(lang, pageTitle,"0");
					String[] line = download.split("\n");
					for (int i = 0; i < line.length; i++) {
						String[] arr = line[i].split("\t");
						//arr[0] pageTitle, arr[1] userName, arr[2] timestamp, arr[3] minor, arr[4] size
						String timestamp = arr[2];
						timestamp = timestamp.replaceAll("T", " ");
						timestamp = timestamp.replaceAll("Z", "");
						data += arr[0] + "\t" + arr[1] + "\t" + timestamp + "\t" + arr[4] + "\n";
						
						// Storing data
						ArticleCache articleCache = new ArticleCache(arr[0],arr[1],df.parse(timestamp),Integer.parseInt(arr[4]));
						PersistenceManager pmWriter = PMF.get().getPersistenceManager();
						try {
							pmWriter.makePersistent(articleCache);
						} finally {
							pmWriter.close();
						}
					}
				} else {
					for(ArticleCache ac:articleCaches) {
						data += ac.getPageTitle() + "\t" + ac.getAuthor() + "\t" + df.format(ac.getDate()) + "\t0\t" + String.valueOf(ac.getSize()) + "\n";
					}
				}
				//log.info(data);
				TimeWindowCount twc = new TimeWindowCount();
				String cvsData = twc.run(data);
				log.info(cvsData);
				String createDate = twc.getCreateDate();
				String allEdits = twc.getAllEdits();
				//String stat = "<p class=\"stat\">Creation: " + createDate + " Edits: " + allEdits + "</p>" + eol;
				String stat = "";

				String script = "";
				String[] cvs = cvsData.split("\n");
				for (int i = 0; i < cvs.length; i++) {
					String date = cvs[i].split(",")[0];
					String edit = cvs[i].split(",")[1];
					String year = date.substring(0,4);
					String month = date.substring(5,7);
					int imonth = Integer.parseInt(month);
					imonth--;
					month = String.valueOf(imonth);
					String day = date.substring(8,10);
					script += "[new Date(" + year + "," + month + "," + day + ")," + edit + "]," + eol;
				}
				ServletContext context = this.getServletContext();
				String skeltonPath = context.getRealPath("/skelton/google.html");
				BufferedReader skelton = new BufferedReader(new FileReader(skeltonPath));
				String line = "";
				while ((line = skelton.readLine()) != null) {
					if (line.startsWith("###script")) { // Insert data at ## line
						output += script + eol;
					} else if (line.startsWith("###stat")) {
						output += stat;
					}else {
						output += line + eol;
					}
				}

			}

			//Set MIME type and Char set
			response.setContentType("text/html; charset=UTF-8");
			//Get output stream
			out = response.getWriter();
			out.println(output);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			if(out != null) {
				try {
					out.close();
				}catch(Exception e){}
			}
		}
	}
}