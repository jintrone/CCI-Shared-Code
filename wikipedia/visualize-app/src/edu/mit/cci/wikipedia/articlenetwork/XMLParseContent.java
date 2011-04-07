package edu.mit.cci.wikipedia.articlenetwork;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

public class XMLParseContent extends DefaultHandler {

	/**
	 * @param args
	 */
	private Result result;
	private String data;
	
	public XMLParseContent(Result _result, String _data) {
		result = _result;
		data = _data;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public void parse() {
		try {
			// Create SAX Parser factory
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			// Create SAX Parser
			SAXParser parser = spfactory.newSAXParser();
			// Process XML file by given default handler
			parser.parse(new ByteArrayInputStream(data.getBytes()), new XMLParseContent(result,data));
			//parser.parse(new File("tmp.xml"), new XMLParseRevId(userName,result,data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Start document
	 */
	public void startDocument() {}
	/**
	 * Start element
	 */
	public void startElement(String uri,
			String localName,
			String qName,
			Attributes attributes) {}
	/**
	 * Read text
	 */
	public void characters(char[] ch,
			int offset,
			int length) {
		result.append(new String(ch,offset,length));
	}
	/**
	 * End element
	 */
	public void endElement(String uri,
			String localName,
			String qName) {}
	/**
	 * End document
	 */
	public void endDocument() {}
}
