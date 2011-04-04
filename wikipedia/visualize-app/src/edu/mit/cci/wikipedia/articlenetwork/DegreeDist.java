package edu.mit.cci.wikipedia.articlenetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DegreeDist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String infile = "Data/Ko/Category_LivingPeople_links_20110214.txt";
		int minDegree = 10;
		try {
			BufferedReader br = new BufferedReader(new FileReader(infile));
			String line = "";
			Map<String,Set<String>> degreeMap = new HashMap<String,Set<String>>();
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				String from = arr[0];
				String to = arr[1];

				// Out degree
				/*if (!degreeMap.containsKey(from)) {
					Set<String> set = new HashSet<String>();
					set.add(to);
					degreeMap.put(from, set);
				} else {
					Set<String> set = degreeMap.get(from);
					if (!set.contains(to))
						set.add(to);
					degreeMap.put(from, set);
				}*/
				
				//In degree
				if (!degreeMap.containsKey(to)) {
					Set<String> set = new HashSet<String>();
					set.add(from);
					degreeMap.put(to, set);
				} else {
					Set<String> set = degreeMap.get(to);
					if (!set.contains(from))
						set.add(from);
					degreeMap.put(to, set);
				}
			}
			//System.out.println(degreeMap.get("Bob Dylan").size());
			Map<Integer,Integer> degree = new TreeMap<Integer,Integer>();
			Iterator<String> it = degreeMap.keySet().iterator();
			while (it.hasNext()) {
				String node = it.next();
				int d = degreeMap.get(node).size();
				if (d > minDegree)
					System.out.println(node + "\t" + d);
				if (!degree.containsKey(d)) {
					degree.put(d, 1);
				} else {
					int v = degree.get(d);
					v++;
					degree.put(d, v);
				}
			}
			Iterator<Integer> iti = degree.keySet().iterator();
			while (iti.hasNext()) {
				int d = iti.next();
				int num = degree.get(d);
				System.out.println(d + "\t" + num);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
