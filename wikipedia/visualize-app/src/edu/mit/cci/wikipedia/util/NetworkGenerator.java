package edu.mit.cci.wikipedia.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NetworkGenerator {
	
	public String getSeqColabNetworkFromRevisions(String data) {
		Map<String,Map<String,Integer>> matrix = new HashMap<String,Map<String,Integer>>();
		Map<String,String> map = new TreeMap<String,String>();
		List<String> list = new LinkedList<String>();
		String edges = "";
		String[] lines = data.split("\n");
		for (String line:lines) {
			String[] arr = line.split("\t");
			String userName = arr[1];
			String timestamp = arr[2];
			map.put(timestamp, userName);
			if (!list.contains(userName))
				list.add(userName);
		}
		String editor = "";
		String prevEditor = "";
		for (String timestamp:map.keySet()) {
			editor = map.get(timestamp);
			if (prevEditor.length() == 0) {
				prevEditor = editor;
				continue;
			}
			String key = "";
			String value = "";
			if (list.indexOf(editor) > list.indexOf(prevEditor)) {
				key = prevEditor;
				value = editor;
			} else {
				key = editor;
				value = prevEditor;
			}
			if (matrix.containsKey(key)) {
				Map<String,Integer> vMap = matrix.get(key);
				if (vMap.containsKey(value)) {
					int weight = vMap.get(value);
					weight++;
					vMap.put(value, weight);
				} else {
					vMap.put(value, 1);
				}
				matrix.put(key, vMap);
			} else {
				Map<String,Integer> vMap = new HashMap<String,Integer>();
				vMap.put(value, 1);
				matrix.put(key, vMap);
			}
			prevEditor = editor;
		}
		
		for (String from : matrix.keySet()) {
			Map<String,Integer> vMap = matrix.get(from);
			for (String to : vMap.keySet()) {
				int weight = vMap.get(to);
				edges += from + "\t" + to + "\t" + String.valueOf(weight) + "\n";
			}
		}
		return edges;
	}

}
