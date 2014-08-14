import java.io.File;   
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;   
import javax.xml.parsers.DocumentBuilderFactory;   

import org.w3c.dom.Document;   
import org.w3c.dom.NodeList;   

public class BookReader {   
	public static List<Book> loadBooks() {   
		try { 
			File f = new File("data/books.xml");   
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
			DocumentBuilder builder = factory.newDocumentBuilder();   
			Document doc = builder.parse(f);   
			NodeList nl = doc.getElementsByTagName("book");   
			List<Book> bookList = new ArrayList<Book>();
			for (int i = 0; i < nl.getLength(); ++i) {
				int id = Integer.parseInt(doc.getElementsByTagName("id").item(i).getFirstChild().getNodeValue());
				String name = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
				String firstTitle = doc.getElementsByTagName("firstTitle").item(i).getFirstChild().getNodeValue();
				String secondTitle = doc.getElementsByTagName("secondTitle").item(i).getFirstChild().getNodeValue();
				String content = doc.getElementsByTagName("content").item(i).getFirstChild().getNodeValue();
				String url = doc.getElementsByTagName("url").item(i).getFirstChild().getNodeValue();
				bookList.add(new Book(id, name, firstTitle, secondTitle, content, url));
			} 
			return bookList;
		} catch (Exception e) {   
			e.printStackTrace();   
		}
		return null;
	}   
} 
