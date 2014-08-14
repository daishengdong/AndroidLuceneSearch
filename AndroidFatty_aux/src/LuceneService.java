import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneService {
    /* ���������ķ����� ��������ʹ�õķִ�������Ͳ�ѯʱ��ʹ�õķִ���һ���������ѯ������Ҫ�Ľ�� */  
    private static Analyzer analyzer = new IKAnalyzer(true);  
    // ��������Ŀ¼  
    private static File indexFile = new File("index/");  

    public static void createIndexFile(List<Book> bookList) {  
        // long startTime = System.currentTimeMillis();  
        // System.out.println("*****************����������ʼ**********************");  
        Directory directory = null;
        IndexWriter indexWriter = null;  
        try {  
            // ��������Ŀ¼����  
            directory = new SimpleFSDirectory(indexFile);  
            indexWriter = new IndexWriter(FSDirectory.open(indexFile), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
  
            // Ϊ�˱����ظ��������ݣ�ÿ�β���ǰ ��ɾ��֮ǰ������  
            indexWriter.deleteAll();  
            // ��ȡʵ�����  
            for (Book book : bookList){  
                // indexWriter�������  
                Document doc = new Document();

                doc.add(new Field("id", Integer.toString(book.getId()),Field.Store.YES, Field.Index.NOT_ANALYZED));  
                doc.add(new Field("name", book.getName().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("firstTitle", book.getFirstTitle().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("secondTitle", book.getSecondTitle().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("content", book.getContent().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("url", book.getUrl().toString(),Field.Store.YES, Field.Index.NOT_ANALYZED));  
                // ��ӵ�������ȥ  
                indexWriter.addDocument(doc);  
                // System.out.println("������ӳɹ�����" + (i + 1) + "�Σ���");  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (indexWriter != null) {  
                try {  
                    indexWriter.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (directory != null) {  
                try {  
                    directory.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        // long endTime = System.currentTimeMillis();
        // System.out.println("���������ļ��ɹ����ܹ�����" + (endTime - startTime) + "���롣");  
        // System.out.println("*****************������������**********************");  
    }  
}
