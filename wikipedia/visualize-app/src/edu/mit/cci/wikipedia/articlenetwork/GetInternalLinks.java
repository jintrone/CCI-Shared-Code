package edu.mit.cci.wikipedia.articlenetwork;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetInternalLinks {
	
	GetInternalLinks(){}
	
	public static void main(String args[]) {
		final String DATETIME = "20110214000000";
		final String INPUT_FILE_PATH = "Data/Es/Category_LivingPeople.txt";
		
		String filePathPrefix = INPUT_FILE_PATH.substring(0, INPUT_FILE_PATH.lastIndexOf("."));
		final String OUTPUT_FILE_PATH = filePathPrefix + "_links_all_" + DATETIME + ".txt";
		final String DONE_CHECK_FILE = filePathPrefix + "_links_all_done.txt";
		
		GetInternalLinks gil = new GetInternalLinks();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
			BufferedWriter bw_done = new BufferedWriter(new FileWriter(DONE_CHECK_FILE));
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Map<String,String> peopleMap = new TreeMap<String,String>();
			//Map<String,Map<String,Integer>> network = new HashMap<String,Map<String,Integer>>();
			String line = "";
			// Loading all id and name in LivingPeople categories
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				if (!peopleMap.containsKey(arr[0])) {
					peopleMap.put(arr[1], arr[0]);
				}
			}
		
			int count = 0;
			Iterator<String> it = peopleMap.keySet().iterator();
			while (it.hasNext()) {
				count++;
				String personName = it.next();
				
				// Display progress
				if (count%100 == 0)
					System.out.println(count + " " + df.format(new Date()));
				
				String personId = peopleMap.get(personName);

				Set<String> linkNames = gil.getIntLinkFromContent(personId,DATETIME);

				Iterator<String> itLinks = linkNames.iterator();

				while (itLinks.hasNext()) {
					String linkName = itLinks.next();
					
					bw.write(personName + "\t" + linkName);
					bw.newLine();
					bw.flush();
				}
				bw_done.write(personName);
				bw_done.newLine();
				bw_done.flush();
				
			}
			bw_done.close();		
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Set<String> getIntLinkFromContent(String personId, String time) {
		//System.out.println(">>" + category);
		// [0]: page_id
		// [1]: page_title
		Set<String> articleSet = new HashSet<String>();
		try {
			String out = "";
			Result result = new Result();
			String xml = getContent(personId,time);
			XMLParseContent parse = new XMLParseContent(result,xml);
			parse.parse();
			out = result.getResult();
			//System.out.println(out);
			
			Pattern pattern = Pattern.compile("\\[{2}.+?\\]{2}",Pattern.DOTALL);
			Matcher matcher = pattern.matcher(out);
			
			while (matcher.find()) {
				String str = matcher.group(matcher.groupCount());
				str = str.replaceAll("\\[", "");
				str = str.replaceAll("\\]", "");
				if (str.indexOf("|") > 0) {
					str = str.substring(0,str.indexOf("|"));
				} else {
					
				}
				//System.out.println(str);
				articleSet.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleSet;
	}
	
	private String getContent(String id, String timestamp) {
		String xml = "";

		id= id.replaceAll(" ", "_");
		try {
			URL url = new URL("http://es.wikipedia.org/w/api.php?format=xml&action=query&prop=revisions&pageids=" + id +"&rvlimit=1&rvprop=content&rvstart=" + timestamp);

			Object content = url.getContent();
			if (content instanceof InputStream) {
				BufferedReader bf = new BufferedReader(new InputStreamReader( (InputStream)content) );        
				String line = "";
				while ((line = bf.readLine()) != null) {
					xml += line + "\n";
				}
			}
			else {
				System.out.println(content.toString());
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Set URL for argument");
			System.exit(-1);
		}
		catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
		return xml;
	}
}

