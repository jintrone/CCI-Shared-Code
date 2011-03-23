package edu.mit.cci.wikipedia.collector;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


import java.io.*;

public class XMLParseUserTalk extends DefaultHandler {

	/**
	 * @param args
	 */
	private static String userName;
	private Result result;
	private String xml;
	
	public XMLParseUserTalk(String _userName, Result _result, String _xml) {
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
		//System.out.println("2:" + userName);
		try {
			// SAXパーサーファクトリを生成
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			// SAXパーサーを生成
			SAXParser parser = spfactory.newSAXParser();
			// XMLファイルを指定されたデフォルトハンドラーで処理します
			parser.parse(new ByteArrayInputStream(xml.getBytes()), new XMLParseUserTalk(userName,result,xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ドキュメント開始時
	 */
	public void startDocument() {
		//System.out.println("ドキュメント開始");
	}
	/**
	 * 要素の開始タグ読み込み時
	 */
	public void startElement(String uri,
			String localName,
			String qName,
			Attributes attributes) {

		if (qName.equals("rev")) {
			if(attributes.getLength()!=0){
				String minor = "0";
				if (attributes.getValue("minor") != null) {
				    minor = "1";
				}else {
				    result.append(getUserName() + "\t" + attributes.getValue("user") + "\t" + attributes.getValue("timestamp") + "\t" + minor + "\n");
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
	 * テキストデータ読み込み時
	 */
	public void characters(char[] ch,
			int offset,
			int length) {

		//System.out.println("テキストデータ：" + new String(ch, offset, length));
	}
	/**
	 * 要素の終了タグ読み込み時
	 */
	public void endElement(String uri,
			String localName,
			String qName) {

		//System.out.println("要素終了:" + qName);
	}
	/**
	 * ドキュメント終了時
	 */
	public void endDocument() {
		//System.out.println("ドキュメント終了");
	}
}
