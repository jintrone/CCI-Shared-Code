package edu.mit.cci.wikipedia.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapSorter {
	public List<String> sortMap(String data, int rank) {
		//log.info(data);
		List<String> output = new LinkedList<String>();
		Hashtable<String,Integer> table = new Hashtable<String,Integer>();
		Hashtable<String,Integer> editSizeTable = new Hashtable<String,Integer>();
		int prevSize  = 0;
		String[] lines = data.split("\n");
		for (int i = 0; i < lines.length; i++) {
			//log.info(i + "\t" + lines[i].split("\t").length + "\t" + lines[i]);
			String[] arr = lines[i].split("\t");
			if (arr.length < 1)
				continue;
			String user = arr[1];
			int size = Integer.parseInt(arr[4]);
			int diff = size - prevSize;
			if (editSizeTable.containsKey(user)) {
				int v = editSizeTable.get(user);
				v += diff;
				editSizeTable.put(user,v);
			} else {
				editSizeTable.put(user,diff);
			}
			prevSize = size;
			if (table.containsKey(user)) {
				int v = table.get(user);
				v++;
				table.put(user, v);
			} else {
				table.put(user, 1);
			}
		}

		// Sort by edit count
		ArrayList al = new ArrayList(table.entrySet());

		Collections.sort(al, new Comparator(){
			public int compare(Object obj1, Object obj2){
				Map.Entry ent1 =(Map.Entry)obj1;
				Map.Entry ent2 =(Map.Entry)obj2;
				return -(((int)Integer.parseInt(ent1.getValue().toString())) - ((int)Integer.parseInt(ent2.getValue().toString())));
			}
		});
		int alsize = al.size();
		if (alsize < rank)
			rank = alsize;
		else if (rank == 0)
			rank = alsize;
		for (int j = 0; j < rank; j++) {
			String str = al.get(j).toString();
			String user = str.substring(0,str.lastIndexOf("="));
			String edits = str.substring(str.lastIndexOf("=")+1);
			String editSize = String.valueOf(editSizeTable.get(user));
			output.add(edits + "\t" + user + "\t" + editSize);
			//log.info(edits + "\t" + user + "\t" + editSize);
		}
		return output;
	}

}
