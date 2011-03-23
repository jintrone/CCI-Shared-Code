package edu.mit.cci.wikipedia.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


public class TimeWindowCount {

	/**
	 * @param args
	 */
	private String createDate;
	private String allEdits;

	public TimeWindowCount(){
		createDate = "";
		allEdits = "";
	}

	private String parseDate(String str) {
		//2004-11-15T23:35:51Z into 2004-11-15 23:35:51
		return str.substring(0,10) + " " + str.substring(11,19);
	}
	public String getCreateDate() {
		return createDate;
	}
	public String getAllEdits() {
		return allEdits;
	}
	public String run(String data) {
		String out = "";
		try {
			String[] lines = data.split("\n");
			String line = lines[0];
			
			List<String> list = new LinkedList<String>();
			String start = parseDate(line.split("\t")[2]);
			createDate = start.substring(0,start.indexOf(" "));
			int count = 1;
			list.add(start);
			for (int i = 1; i < lines.length; i++) {
				count++;
				list.add(parseDate(lines[i].split("\t")[2]));
			}
			out = timeWindow(start,list);
			allEdits = String.valueOf(count);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	// start:oldest date-time, lines: list of edit (revision) history
	private String timeWindow(String start, List<String> lines) {
		String output = "";
		int step = Calendar.MONTH; // Length of window: month
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			Date initTime =  df.parse(start);
			Date endTime = new Date();

			Calendar initCal = new GregorianCalendar();
			initCal.setTime(initTime);
			// Each time-window starts day 1st of a month
			initCal.set(Calendar.DATE, 1);
			initCal.set(Calendar.HOUR_OF_DAY, 0);
			initCal.set(Calendar.MINUTE, 0);
			initCal.set(Calendar.SECOND, 0);
			initTime = initCal.getTime();

			Calendar nextCal = (Calendar)initCal.clone();
			// Move forward by the step
			nextCal.add(step, 1);
			Date nextTime = nextCal.getTime();

			while (initTime.before(endTime)) {
				// initTime: start point of time-window
				// nextTime: end point of time-window
				int count = 0;
				
				// Go through the list and count edits which made in the time-window
				for (int i = 0; i < lines.size(); i++) {
					Date tmp = df.parse(lines.get(i));
					if (tmp.compareTo(initTime) >= 0 && tmp.compareTo(nextTime) < 0) {
						// Count edits in each time-window
						count++;
					}
				}
				// datetime of a time-window \tab # of edits
				output += df.format(initTime) + "," + count + "\n";

				// Move 1 month ahead
				initCal.setTime(initTime);
				initCal.add(step, 1);
				initTime = initCal.getTime();

				nextCal.setTime(nextTime);
				nextCal.add(step,1);
				nextTime = nextCal.getTime();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}
