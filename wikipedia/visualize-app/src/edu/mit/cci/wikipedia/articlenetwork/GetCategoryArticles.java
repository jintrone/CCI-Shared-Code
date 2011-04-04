package edu.mit.cci.wikipedia.articlenetwork;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class GetCategoryArticles {
	
	GetCategoryArticles(){}
	
	public static void main(String args[]) {
		final String ROOT_CATEGORY = "Categor%C3%ADa:Personas_vivas";
		final String LANG = "es";
		final String OUTPUT_FILE_PATH = "Data/Es/Category_LivingPeople.txt";
		final boolean RECURSIVE = false;
		
		GetCategoryArticles gca = new GetCategoryArticles();
		List<String[]> memberList = gca.getMembers(ROOT_CATEGORY, LANG, RECURSIVE);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
			for (String[] elem : memberList) {
				bw.write(elem[0] + "\t" + elem[1] + "\t" + elem[2]);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String[]> getMembers(String rootCategory, String lang, boolean subcategory) {

		List<String[]> memberList = new ArrayList<String[]>();
		List<String> cat_list = new LinkedList<String>();
		cat_list.add(rootCategory);
		while (true) {
			List<String> cat_list_2 = new LinkedList<String>();
			for (int l = 0; l < cat_list.size(); l++) {
				List<String[]> list = getCategoryMember(cat_list.get(l), lang);
				for (int i = 0; i < list.size(); i++) {
					String[] data = list.get(i);
					//System.out.println(data.length);
					//System.out.println(data[0] + "\t" + data[1] + "\t" + data[2]);
					if (data.length < 3) {
						//System.out.println(list.get(i));
						continue;
					}
					if (data[2].equals("14")) {
						if (subcategory)
							cat_list_2.add(data[1]);
					} else if (data[2].equals("0")){
						memberList.add(data);
					}
				}
			}
			cat_list = cat_list_2;
			if (cat_list_2.size() == 0)
				break;
		}
		return memberList;
	}
	
	public List<String[]> getCategoryMember(String category, String lang) {
		//System.out.println(">>" + category);
		// [0]: page_id
		// [1]: page_title
		// [2]: ns
		List<String[]> articleList = new ArrayList<String[]>();
		HashSet<String> set = new HashSet<String>();
		try {
			String out = "";
			Result result = new Result();
			String xml = getCategoryMembersXML(category, lang, "");
			XMLParseCategory parse = new XMLParseCategory(result,xml);
			parse.parse();
			while (result.hasNextId()) {
				out += result.getResult();
				String nextId = result.getNextId();
				nextId = nextId.replaceAll(" ", "_");
				result.clear();
				
				xml = getCategoryMembersXML(category, lang, nextId);
				parse = new XMLParseCategory(result,xml);
				parse.parse();
			}
			out += result.getResult();
			String[] lines = out.split("\n");
			for (int i = 0; i < lines.length; i++) {
				String[] arr = lines[i].split("\t");
				articleList.add(arr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleList;
	}
	
	
	private String getCategoryMembersXML(String category, String lang, String nextId) {
		String xml = "";
		String ext = "";
		if (nextId.length() > 0) {
			nextId = nextId.replaceAll("&", "%26");
			ext = "&cmcontinue=" + nextId;
		}
		category = category.replaceAll(" ", "_");
		try {
			URL url = new URL("http://" + lang + ".wikipedia.org/w/api.php?format=xml&action=query&cmlimit=500&list=categorymembers&cmtitle=" + category + ext);
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

