package edu.mit.cci.wikipedia.util;

import java.io.*;
import java.util.*;

public class Processing {


	public Processing(){}

	//public String processingCode(String location,String edges, String loginUser, String path, String userName_editSize, String size) {
	public String processingCode(String nodes,String edges, String path, String size) {
		//List<String> user = new LinkedList<String>();
		String code = "";
		String eol = "\n";
		String firstName = "";
		String nodeCode = "";
		String[] nodeArr = nodes.split("\n");
		for (String node:nodeArr) {
			//engine.addParticle(new Particle("Remco", random(0, canvasSize), random(0, canvasSize), 30, 0, 0, 0x80FF0000));
			String name = node.split("\t")[0];
			if (firstName.length() == 0)
				firstName = name;
			
			String[] colors = {"0x800000FF","0x80FF0000", "0x8000FF00"};

			double nodeSize = Double.parseDouble(node.split("\t")[1]);
			nodeSize = Math.log10(nodeSize) * 20;
			int numOfArticles = Integer.parseInt(node.split("\t")[2]);
			String color = "";
			if (numOfArticles < 4) {
				color = colors[numOfArticles-1];
			} else {
				color = "0x80000000";
			}
			
			nodeCode += "engine.addParticle(new Particle(\"" + name + "\", random(0, " + size + "), random(0, " + size + "), " + nodeSize + ", 0, 0, " + color + "));" + eol;
		}
		
		String edgeCode = "";
		String[] edgeArr = edges.split("\n");
		for (String edge:edgeArr) {
			//engine.connectParticles("Remco", "Joris", 2);
			String name1 = edge.split("\t")[0];
			String name2 = edge.split("\t")[1];
			double thick = Double.parseDouble(edge.split("\t")[2]);
			thick = Math.log10(thick) * 2;
			edgeCode += "engine.connectParticles(\"" + name1 + "\", \"" + name2 + "\", " + thick + ");" + eol;
			
		}
		try {
			// data = user_name\t# of edits\tx_position\ty_position
			/*String[] datum = location.split("\n");
			for (int i = 0; i < datum.length; i++) {
				user.add(datum[i].split("\t")[0]);
			}*/
			// Read skeleton code
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#SIZE")) {
					code += "int canvasSize = " + size + ";" + eol;
				} else if (line.startsWith("#NODES")) {
					code += nodeCode;
				} else if (line.startsWith("#EDGES")) {
					code += edgeCode;
				} else if (line.startsWith("#PINNED")) {
					//engine.findParticle("Joris").pin(canvasSize/2, canvasSize/2);
					code += "engine.findParticle(\"" + firstName + "\").pin(" + size + "/2, " + size + "/2);" + eol;
				} else {
					code += line + eol;
				}
				/*if (line.startsWith("###ARRAY")) {
					code += "int count = " + datum.length + ";" + eol;
					code += "String me = \"" + loginUser + "\";" + eol;
					code += "float[][] e = new float[" + datum.length + "][3];" + eol;
					code += "String[] label = new String[" + datum.length + "];" + eol;
				}else if (line.startsWith("###SIZE")) {
					code += "size(" + canvasHeight + "," + canvasWidth +");" + eol;
				}else if (line.startsWith("###NODES")) {
					for (int i = 0; i < datum.length; i++) {
						String[] arr = datum[i].split("\t");
						double x = Double.parseDouble(arr[2]); // x position
						double y = Double.parseDouble(arr[3]); // y position
						int size = Integer.parseInt(arr[1]);
						// dsize:: Node size
						double dsize = Math.log10((double)size);
						dsize = 10 + 10* dsize * dsize;
						code += "e[" + i +"][0]=" + x + ";" + eol; // X pos
						code += "e[" + i +"][1]=" + y + ";" + eol; // Y pos
						code += "e[" + i +"][2]=" + dsize + ";" + eol; // Size
						code += "label[" + i +"]=\"" + arr[0] + "\";" + eol;
					}
				} else if (line.startsWith("###EDGES")) {
					String[] edge = edges.split("\n");
					for (int i = 0; i < edge.length; i++) {
						String[] arr = edge[i].split("\t");
						int source = user.indexOf(arr[0]);
						int target = user.indexOf(arr[1]);
						int weight = Integer.parseInt(arr[2]);
						double strokeWeight = 1 + Math.log10(weight)*1.5;
						code += "strokeWeight(" + strokeWeight + ");" + eol;
						code += "line(e[" + source + "][0],e[" + source + "][1],e[" + target +"][0],e[" + target + "][1]);" + eol;
					}
					code += "strokeWeight(2);" + eol;
				}else {
					code += line + eol;
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(code);
		return code;
	}
}