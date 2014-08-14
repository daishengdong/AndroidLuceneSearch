package com.fatty.po;

import java.io.File;   
import java.io.FileInputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Xml;

@SuppressLint("UseValueOf")
public class BookReader {   
	public static ArrayList<Book> loadBooks() {
		try {
			String path = Environment.getExternalStorageDirectory()
					+ File.separator + "fatty" + File.separator + "books.xml";
			File xmlFile = new File(path);   
			FileInputStream inputStream = new FileInputStream(xmlFile);  
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8"); // ��������Դ����
			int eventType = parser.getEventType(); // ��ȡ�¼�����
			Book currentBook = null;
			ArrayList<Book> books= null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// �ĵ���ʼ�¼�,���Խ������ݳ�ʼ������
					books = new ArrayList<Book>();// ʵ����������
					break;
				case XmlPullParser.START_TAG://��ʼ��ȡĳ����ǩ
					//ͨ��getName�ж϶����ĸ���ǩ��Ȼ��ͨ��nextText()��ȡ�ı��ڵ�ֵ����ͨ��getAttributeValue(i)��ȡ���Խڵ�ֵ
					String name = parser.getName();
					if (name.equalsIgnoreCase("book")) {
						currentBook = new Book();
					} else if (currentBook != null) {
						if (name.equalsIgnoreCase("id")) {
							currentBook.setId(new Integer(parser.nextText()));
						} else if (name.equalsIgnoreCase("name")) {
							currentBook.setName(parser.nextText());// ���������TextԪ��,����������ֵ
						} else if (name.equalsIgnoreCase("firstTitle")) {
							currentBook.setFirstTitle(parser.nextText());
						} else if (name.equalsIgnoreCase("secondTitle")) {
							currentBook.setSecondTitle(parser.nextText());
						} else if (name.equalsIgnoreCase("content")) {
							currentBook.setContent(parser.nextText());
						} else if (name.equalsIgnoreCase("url")) {
							currentBook.setUrl(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:// ����Ԫ���¼�
					//����һ��Person�����Խ�����ӵ���������
					if (parser.getName().equalsIgnoreCase("book") && currentBook != null) {
						books.add(currentBook);
						currentBook= null;
					}
					break;
				}
				eventType = parser.next();
			}
			inputStream.close();
			return books;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
} 
