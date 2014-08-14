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
    /* 创建简单中文分析器 创建索引使用的分词器必须和查询时候使用的分词器一样，否则查询不到想要的结果 */  
    private static Analyzer analyzer = new IKAnalyzer(true);  
    // 索引保存目录  
    private static File indexFile = new File("index/");  

    public static void createIndexFile(List<Book> bookList) {  
        // long startTime = System.currentTimeMillis();  
        // System.out.println("*****************创建索引开始**********************");  
        Directory directory = null;
        IndexWriter indexWriter = null;  
        try {  
            // 创建磁盘目录对象  
            directory = new SimpleFSDirectory(indexFile);  
            indexWriter = new IndexWriter(FSDirectory.open(indexFile), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
  
            // 为了避免重复插入数据，每次测试前 先删除之前的索引  
            indexWriter.deleteAll();  
            // 获取实体对象  
            for (Book book : bookList){  
                // indexWriter添加索引  
                Document doc = new Document();

                doc.add(new Field("id", Integer.toString(book.getId()),Field.Store.YES, Field.Index.NOT_ANALYZED));  
                doc.add(new Field("name", book.getName().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("firstTitle", book.getFirstTitle().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("secondTitle", book.getSecondTitle().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("content", book.getContent().toString(),Field.Store.YES, Field.Index.ANALYZED));  
                doc.add(new Field("url", book.getUrl().toString(),Field.Store.YES, Field.Index.NOT_ANALYZED));  
                // 添加到索引中去  
                indexWriter.addDocument(doc);  
                // System.out.println("索引添加成功：第" + (i + 1) + "次！！");  
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
        // System.out.println("创建索引文件成功，总共花费" + (endTime - startTime) + "毫秒。");  
        // System.out.println("*****************创建索引结束**********************");  
    }  
}
