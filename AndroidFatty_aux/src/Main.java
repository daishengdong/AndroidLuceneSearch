import java.util.List;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Book> bookList = BookReader.loadBooks();
		LuceneService.createIndexFile(bookList);
		System.out.println("done");
	}

}
