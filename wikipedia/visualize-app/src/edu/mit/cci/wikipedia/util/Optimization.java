package edu.mit.cci.wikipedia.util;

import java.util.Hashtable;


public class Optimization {

	/**
	 * @param args
	 */
	private String[] people;
	private String[][] links;

	public String run(String nodes, String edges, int domainHeight, int domainWidth) {
		String output = "";

		// Initialize people[] and links[][]
		String[] node = nodes.split("\n");
		people = new String[node.length];
		for (int i = 0; i < node.length; i++) {
			people[i]  = node[i].split("\t")[0]; // [0]: User name, [1]: # of edits
		}

		String[] edge = edges.split("\n");
		links = new String[edge.length][3];
		for (int i = 0; i < edge.length; i++) {
			String[] arr = edge[i].split("\t");
			links[i][0] = arr[0]; // From user name
			links[i][1] = arr[1]; // To user name
			links[i][2] = arr[2]; // # of connections
		}

		// Canvas size is domainHeigh x domainWidth
		int[][] domain = new int[people.length*2][2];
		for (int i = 0; i < domain.length; i++) {
			domain[i][0] = 10; // Min value
			domain[i][1] = domainHeight-10; // Max value
			i++;
			domain[i][0] = 10;
			domain[i][1] = domainWidth-10;
		}

		double[] vec = annealingoptimize(domain, 10000, 0.95, 30);
		//String[] node = nodes.split("\n");
		for (int i = 0; i < node.length; i++) {
			String[] arr = node[i].split("\t");
			double x = vec[2*i];
			double y = vec[2*i+1];
			output += arr[0] + "\t" + arr[1] + "\t" + x + "\t" + y + "\n";
			//System.out.println(arr[0] + "\t" + arr[1] + "\t" + x + "\t" + y);
		}
		return output;
	}

	// Refer to "Collective Intelligence Programming Chapter 5-10 visualizing network" 
	public double[] annealingoptimize(int[][] domain, double T, double cool, int step) {

		// Initialize coordinates with random values
		double vec[] = new double[domain.length];
		for (int i = 0; i < domain.length; i++) {
			vec[i] = getRandDouble(domain[i][0],domain[i][1]);
		}

		while (T > 0.1) {
			// Choose one index
			int i = getRandInt(0,domain.length-1);

			// インデックスの値に加える変更の方向を選ぶ
			int dir = getRandInt(-1*step,step);
			//System.out.println("dir " + dir);

			// 値を変更したリストを生成
			double vecb[] = new double[vec.length];
			for (int v = 0; v < vec.length; v++) {
				vecb[v] = vec[v];
			}
			vecb[i] += dir;
			if (vecb[i] < domain[i][0]) {
				vecb[i] = domain[i][0];
			} else if (vecb[i] > domain[i][1]) {
				vecb[i] = domain[i][1];
			}

			// 現在解と生成解のコストを算出
			double ea = crosscount(vec);
			double eb = crosscount(vecb);
			double p = Math.pow(Math.E,-1*Math.abs(eb-ea)/T);

			// 生成解がベター？　または確率的に採用？
			if (eb < eb || Math.random() < p) {
				vec = vecb;
			}

			// 温度を下げる
			T = T * cool;
		}

		return vec;	
	}



	private int crosscount(double[] locations) {
		int total = 0;
		Hashtable<String,double[]> loc = new Hashtable<String,double[]>();
		for (int i = 0; i < people.length; i++) {
			double[] location = new double[2];
			location[0] = locations[2*i];
			location[1] = locations[2*i+1];
			loc.put(people[i], location);
			//System.out.println(i + " " + people[i] + "\t" + location[0] + " " + location[1]);
		}

		for (int i = 0; i < links.length; i++) {
			for (int j = i+1; j < links.length; j++) {
				double x1 = loc.get(links[i][0])[0];
				double y1 = loc.get(links[i][0])[1];

				double x2 = loc.get(links[i][1])[0];
				double y2 = loc.get(links[i][1])[1];

				double x3 = loc.get(links[j][0])[0];
				double y3 = loc.get(links[j][0])[1];

				double x4 = loc.get(links[j][1])[0];
				double y4 = loc.get(links[j][1])[1];

				double den = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
				if (den == 0)
					continue;

				double ua = ((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/den;
				double ub = ((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/den;
				// 両方の線で分点が0から1の間にあれば線は交差している
				if (ua > 0 && ua < 1 && ub > 0 && ub < 1) {
					total += 1;
				}
			}
		}
		for (int i = 0; i < people.length; i++) {
			for (int j = i+1; j < people.length; j++) {
				// Get nodes' coordinates
				double x1 = loc.get(people[i])[0];
				double y1 = loc.get(people[i])[1];

				double x2 = loc.get(people[j])[0];
				double y2 = loc.get(people[j])[1];

				// Calculate distans between nodes
				double dist = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
				double idealDist = 0;
				for (int k = 0; k < links.length; k++) {
					String user1 = links[k][0];
					String user2 = links[k][1];
					if (user1.equals(people[i]) && user2.equals(people[j])) {
						idealDist = Double.parseDouble(links[k][2]);
					}

				}

				// If dist is less than 50 pixels, then add penalty
				if (idealDist == 0 && dist < 100)
					total += 1;
				//total += (1.5-(dist/100.0));
			}
		}
		return total;
	}

	private int getRandInt(int a, int b) {
		double r = Math.random();
		int len = b - a;
		r = r * (double)len;
		r = a + r;
		return (int)r;
	}
	private double getRandDouble(int a, int b) {
		double r = Math.random();
		int len = b - a;
		r = r * (double)len;
		r = a + r;
		return r;
	}
}