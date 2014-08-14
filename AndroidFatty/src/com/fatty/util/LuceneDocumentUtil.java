package com.fatty.util;

import org.apache.lucene.document.Document;

import com.fatty.po.Book;

public class LuceneDocumentUtil {

	/**
	 * 
	 * @function:将document对象转成book对象
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param doc
	 * @return Book
	 */
	public static Book DocumentToBook(Document doc) {
		Book book = new Book();
		book.setId(Integer.parseInt(doc.get("id")));
		book.setName(doc.get("name"));
		book.setFirstTitle(doc.get("firstTitle"));
		book.setSecondTitle(doc.get("secondTitle"));
		book.setContent(doc.get("content"));
		book.setUrl(doc.get("url"));
		return book;
	}

}
