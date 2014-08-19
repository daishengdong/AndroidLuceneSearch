package com.fatty.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKQueryParser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.fatty.adapter.ListViewAdapter;
import com.fatty.po.Book;
import com.fatty.util.HtmlUrlUtil;
import com.fatty.util.LuceneConfigurationUtil;
import com.fatty.util.LuceneDocumentUtil;
import com.fatty.util.LuceneUtil;
import com.fatty.util.LuneceHighlighterUtil;

public class LuceneService {
	private final class AsyncTaskExtension extends AsyncTask {
		private String[] key;
		private String value;
        private ArrayList<Book> books = null;
		
		public AsyncTaskExtension(String[] key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		protected Object doInBackground(Object... params) {
			books = new ArrayList<Book>();
			IndexSearcher indexSearcher = null;
			QueryParser queryParser = null;
			Query query = null;
			try {
				// 通过工具类获取indexSearcher
				indexSearcher = LuceneUtil.getIndexSearcher();

				// 用IKQueryParser解析要查询的内容，并返回Query对象
				// query = IKQueryParser.parseMultiField(key, value);

				/***************************/
				queryParser = new MultiFieldQueryParser(Version.LUCENE_30, key, LuceneConfigurationUtil.getAnalyzer());
				// 利用queryParser解析传递过来的检索关键字，完成Query对象的封装
				query = queryParser.parse(value);
				/***************************/

				// 按照id进行排序
				Sort sort = new Sort(new SortField("id", SortField.INT, true));

				TopDocs topDocs = indexSearcher.search(query, null, 1000, sort);

				// ScoreDoc 存储了文档的逻辑编号和文档积分
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				for (ScoreDoc temp : scoreDocs) {
					// 根据doc的id去获取doc文档 ,返回是Document
					Document doc = indexSearcher.doc(temp.doc);

					// 通过工具类设置关键字高亮
					doc.getField("name").setValue(
							LuneceHighlighterUtil.highlighterText(query,
									doc.get("name"), 10, value));

					doc.getField("firstTitle").setValue(
							LuneceHighlighterUtil.highlighterText(query,
									doc.get("firstTitle"), 30, value));

					doc.getField("secondTitle").setValue(
							LuneceHighlighterUtil.highlighterText(query,
									doc.get("secondTitle"), 30, value));

					doc.getField("content").setValue(
							LuneceHighlighterUtil.highlighterText(query,
									doc.get("content"), 30, value));

					books.add(LuceneDocumentUtil.DocumentToBook(doc));
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return books;
		}

		protected void onPostExecute(Object result) {
			// 调用业务层方法将搜索结果添加到ListView
			addBookToListView(books);
		}
	}

	private Context context = null;
	private ListView bookListView = null;
	private ArrayList<String> url = null;

	// 定义各个查询条件的常量
	private final static String[] TITLE = { "name" };
	private final static String[] FIRSTTITLE = { "firstTitle" };
	private final static String[] SECONDTITLE = { "secondTitle" };
	private final static String[] CONTENT = { "content" };
	private final static String[] ALL = { "name", "firstTitle", "secondTitle", "content" };

	public LuceneService(Context context, ListView bookListView) {
		super();
		this.context = context;
		this.bookListView = bookListView;
	}

	/**
	 * 
	 * @function:将书本信息添加到ListView
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param books
	 */
	public void addBookToListView(ArrayList<Book> books) {
		int count = books.size();
		if (count <= 0) {
			Toast.makeText(this.context, "没有返回数据！", Toast.LENGTH_SHORT).show();
			return;
		}
		// listItem用于存放books
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		// 每条书本信息对应的详细信息连接
		this.url = new ArrayList<String>();
		// 循环遍历books，按键值对的形式存入HashMap
		for (Book book : books) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", book.getName());
			item.put("firstTitle", book.getFirstTitle());
			item.put("secondTitle", book.getSecondTitle());
			item.put("content", book.getContent());
			url.add(HtmlUrlUtil.getUrl(book.getUrl()));
			// url.add(book.getUrl());

			// 把item项的数据加到listItem中
			listItem.add(item);
		}

		// 新建自定义ViewAdapter
		ListViewAdapter listViewAdapter = new ListViewAdapter(this.context, listItem);

		// 把适配器装载到listView中
		this.bookListView.setAdapter(listViewAdapter);

		// 为每个item项注册点击事件
		this.bookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 当用户点击某个item项时，用浏览器打开网页，查看更详细的信息
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse(url.get(arg2)));
				intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				context.startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * @function:获取查询条件
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param item
	 * @return String[]
	 */
	public String[] getIndexSearch(long item) {
		if (item == 0) {
			// 按书名查找
			return TITLE;
		} else if (item == 1) {
			// 按章查找
			return FIRSTTITLE;
		} else if (item == 2) {
			// 按节查找
			return SECONDTITLE;
		} else if (item == 3) {
			// 按内容查找
			return CONTENT;
		} else {
			// 查找全部
			return ALL;
		}
	}

	/**
	 * 
	 * @function:查询书本信息
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param key
	 * @param value
	 * @return ArrayList
	 */
	public ArrayList<Book> queryBook(String[] key, String value) {
		new AsyncTaskExtension(key, value).execute();

		return null;
	}

}
