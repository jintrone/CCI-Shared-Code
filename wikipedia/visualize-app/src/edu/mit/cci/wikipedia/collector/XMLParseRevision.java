package edu.mit.cci.wikipedia.collector;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


import java.io.*;

public class XMLParseRevision extends DefaultHandler {

	/**
	 * @param args
	 */
	private static String userName;
	private Result result;
	private String xml;

	public XMLParseRevision(String _userName, Result _result, String _xml) {
		userName = _userName;
		result = _result;
		xml = _xml;
	}

	public void setUserName(String _userName) {
		userName = _userName;
	}
	public String getUserName() {
		return userName;
	}

	public void parse() {
		try {
			// Create SAX Parser Factory
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			// Generate SAX Parser
			SAXParser parser = spfactory.newSAXParser();
			parser.parse(new ByteArrayInputStream(xml.getBytes()), new XMLParseRevision(userName,result,xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Document start
	 */
	public void startDocument() {
		//System.out.println("Document start");
	}
	/**
	 * Reading the element start tag
	 */
	public void startElement(String uri,
			String localName,
			String qName,
			Attributes attributes) {

		//System.out.println("Element:" + qName); 
		if (qName.equals("rev")) {
			if(attributes.getLength()!=0){
				String minor = "0";
				if (attributes.getValue("minor") != null) {
					minor = "1";
				} else {
					String user = attributes.getValue("user");
					if (user.split("\\.").length != 4) // Remove IP editors (xxx.xxx.xxx.xxx)
						result.append(getUserName() + "\t" + attributes.getValue("user") + "\t" + attributes.getValue("timestamp") + "\t" + minor + "\t" + attributes.getValue("size") + "\n");
				}
			}
		}
		if (qName.equals("revisions")) {
			if(attributes.getLength() > 0) {	
				//System.out.println("\trevision continues " + attributes.getValue("rvstartid"));
				result.setNextId(attributes.getValue("rvstartid"));
			}
		}
	}
	/**
	 * Read text data
	 */
	public void characters(char[] ch,
			int offset,
			int length) {

		//System.out.println("Text：" + new String(ch, offset, length));
	}
	/**
	 * Read the element end tag
	 */
	public void endElement(String uri,
			String localName,
			String qName) {

		//System.out.println("Element end:" + qName);
	}
	/**
	 * End document
	 */
	public void endDocument() {
		//System.out.println("Document end");
	}
}

