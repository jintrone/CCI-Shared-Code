package edu.mit.cci.wikipedia.articlenetwork;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

public class XMLParseCategory extends DefaultHandler {

	/**
	 * @param args
	 */
	private Result result;
	private String xml;
	
	public XMLParseCategory(Result _result, String _xml) {
		result = _result;
		xml = _xml;
	}
	
	public void parse() {
		try {
			// Create SAX Parser factory
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			// Create SAX Parser
			SAXParser parser = spfactory.newSAXParser();
			// Process XML file by given default handler
			parser.parse(new ByteArrayInputStream(xml.getBytes()), new XMLParseCategory(result,xml));
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
			Attributes attributes) {
		//   <cm pageid="7630466" ns="0" title="1987 Miami Hurricanes football team" />
		if (qName.equals("cm")) {
			if(attributes.getLength()!=0){
				result.append(attributes.getValue("pageid") + "\t" + attributes.getValue("title") + "\t" + attributes.getValue("ns") + "\n");
			}
		}
		// <categorymembers cmcontinue="Buckingham Palace|" />
		if (qName.equals("categorymembers")) {
			if(attributes.getLength() > 0){
				//System.out.println(attributes.getValue("cmcontinue"));
				result.setNextId(attributes.getValue("cmcontinue"));
			}
		}
	}
	/**
	 * Read text
	 */
	public void characters(char[] ch,
			int offset,
			int length) {
		//System.out.println("Text data: " + new String(ch, offset, length));
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
