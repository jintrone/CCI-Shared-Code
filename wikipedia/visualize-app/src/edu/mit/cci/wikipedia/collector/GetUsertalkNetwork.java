package edu.mit.cci.wikipedia.collector;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.w3c.dom.Node;



public class GetUsertalkNetwork {
	
	private static final Logger log = Logger.getLogger(GetUsertalkNetwork.class.getName());

	/**
	 * @param args
	 */

	public static String getNetwork(String lang, String nodes) {
		String data = "";

		Result result = new Result();
		try {
			String[] node = nodes.split("\n");
			int[][] matrix = new int[node.length][node.length];
			for (int i = 0; i < node.length; i++) {
				for (int j = 0; j < node.length; j++) {
					if (i == j)
						continue;
					String from = node[i].split("\t")[0];
					from = from.replaceAll(" ","_");
					String to = node[j].split("\t")[0];
					to = to.replaceAll(" ", "_");

					String xml = getUserTalkContribs(lang, to,from,"");
					XMLParseUserTalk parse = new XMLParseUserTalk(to,result,xml);
					parse.parse();
					String out = result.getResult();
					
					//log.info(i + "-" + from + " " + j + "-" + to + " " + out);
					if (out.length() > 1)
						matrix[i][j] = (out.split("\n").length);
					//System.out.println("To:" + to + "¥tFrom:" + from + "¥tCount:" + matrix[i][j]);
					result.clear();
					
				}
			}
			for (int i = 0; i < matrix.length; i++) {
				for (int j = i+1; j < matrix[i].length; j++) {
					int value = matrix[i][j] + matrix[j][i];
					if (value > 0)
						data += node[i].split("\t")[0] + "\t" + node[j].split("\t")[0] + "\t" + String.valueOf(value) + "\n";
				}
			}
			//log.info(data);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private static String getUserTalkContribs(String lang, String to, String from, String nextId) {
		// TODO Auto-generated method stub
		String rvstartid = "&rvstartid=" + nextId;
		if (nextId.equals("")) {
			rvstartid = "";
		}
		String xml = "";
		try {
			to = URLEncoder.encode(to,"UTF-8");
			from = URLEncoder.encode(from,"UTF-8");
			//String urlStr = "http://en.wikipedia.org/w/api.php";
			String urlStr = "http://" + lang + ".wikipedia.org/w/api.php?format=xml&action=query&prop=revisions&titles=User_talk:" + to + "&rvlimit=500&rvprop=flags%7Ctimestamp%7Cuser&rvuser=" + from + rvstartid;
			//log.info(urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setRequestMethod("GET");
			urlCon.setInstanceFollowRedirects(false);

			urlCon.connect();


			BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				//tmpData.append(line + "\n");
				xml += line + "\n";
			}
			//log.info(xml);
			reader.close();

		} catch (MalformedURLException e) {
			// ...
			log.info(e.getMessage());
		} catch (IOException e) {
			// ...
			log.info(e.getMessage());
		}
		return xml;
	}
}

