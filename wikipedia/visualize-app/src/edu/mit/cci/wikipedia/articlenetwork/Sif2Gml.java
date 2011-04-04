package edu.mit.cci.wikipedia.articlenetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Sif2Gml {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String INPUT_FILE_PATH = "Data/Es/Category_LivingPeople_links_20110214.txt";
		final int MIN_DEGREE = 50;
		try {
			BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			BufferedWriter bw = new BufferedWriter(new FileWriter(INPUT_FILE_PATH + "_indegree_" + MIN_DEGREE + ".gml"));
			bw.write("graph\n");
			bw.write("[\n");
			bw.write("directed 1\n");
			bw.flush();
			String line = "";
			int count = 0;
			
			// In-degree matrix key:target_page, value:source_page
			Map<String,Map<String,String>> matrix = new HashMap<String,Map<String,String>>();
			br = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				String from = arr[0];
				String to = arr[1];
				
				if (matrix.containsKey(to)) {
					Map<String,String> m = matrix.get(to);
					m.put(from, arr[2]);
					matrix.put(to, m);
				} else {
					Map<String,String> m = new HashMap<String,String>();
					m.put(from, arr[2]);
					matrix.put(to, m);
				}
			}
			br.close();
			System.out.println("Matrix done " + matrix.size());
			
			Map<Integer,String> map = new TreeMap<Integer,String>();
			Map<String,Integer> keymap = new TreeMap<String,Integer>();
			Iterator<String> it = matrix.keySet().iterator();
			while (it.hasNext()) {
				String from = it.next();
				Map<String,String> m = matrix.get(from);
				int degree = m.size();
				
				if (degree < MIN_DEGREE)
					continue;
				
				if(!keymap.containsKey(from)) {
					keymap.put(from, count);
					map.put(count,from);
					count++;
				}
				
				Iterator<String> it2 = m.keySet().iterator();
				while (it2.hasNext()) {
					String to = it2.next();
					
					if (!keymap.containsKey(to)) {
						keymap.put(to, count);
						map.put(count,to);
						count++;
					}
				}
			}

			Iterator<Integer> iti = map.keySet().iterator();
			while (iti.hasNext()) {
				int id = iti.next();
				String value = map.get(id);
				bw.write("node\n");
				bw.write("[\n");
				bw.write("id " + id + "\n");
				value = value.replaceAll("\"", "\\\\\"");
				bw.write("label \"" + value + "\"\n");
				bw.write("]\n");
				bw.flush();
				
			}
			int edges = 0;
			// In-degree matrix
			it = matrix.keySet().iterator();
			while (it.hasNext()) {
				String to = it.next();
				Map<String,String> v = matrix.get(to);
				int degree = v.size();
				if (degree < MIN_DEGREE)
					continue;
				Iterator<String> it2 = v.keySet().iterator();
				while (it2.hasNext()) {
					
					String from = it2.next();
					String tie = v.get(to);
					
					if (from.equals(to))
						continue;
					bw.write("edge\n");
					bw.write("[\n");
					bw.write("source " + keymap.get(from) + "\n");
					bw.write("target " + keymap.get(to) + "\n");
					bw.write("value " + tie + "\n");
					bw.write("]\n");
					bw.flush();
					edges++;
				}
				bw.flush();
				
			}
			System.out.println(edges);
			bw.write("]\n");
			bw.flush();
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
