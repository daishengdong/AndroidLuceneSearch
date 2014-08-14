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
			parser.setInput(inputStream, "UTF-8"); // 设置数据源编码
			int eventType = parser.getEventType(); // 获取事件类型
			Book currentBook = null;
			ArrayList<Book> books= null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					books = new ArrayList<Book>();// 实例化集合类
					break;
				case XmlPullParser.START_TAG://开始读取某个标签
					//通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值
					String name = parser.getName();
					if (name.equalsIgnoreCase("book")) {
						currentBook = new Book();
					} else if (currentBook != null) {
						if (name.equalsIgnoreCase("id")) {
							currentBook.setId(new Integer(parser.nextText()));
						} else if (name.equalsIgnoreCase("name")) {
							currentBook.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
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
				case XmlPullParser.END_TAG:// 结束元素事件
					//读完一个Person，可以将其添加到集合类中
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
