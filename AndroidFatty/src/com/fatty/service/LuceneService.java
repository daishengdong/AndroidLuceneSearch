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
				// ͨ���������ȡindexSearcher
				indexSearcher = LuceneUtil.getIndexSearcher();

				// ��IKQueryParser����Ҫ��ѯ�����ݣ�������Query����
				// query = IKQueryParser.parseMultiField(key, value);

				/***************************/
				queryParser = new MultiFieldQueryParser(Version.LUCENE_30, key, LuceneConfigurationUtil.getAnalyzer());
				// ����queryParser�������ݹ����ļ����ؼ��֣����Query����ķ�װ
				query = queryParser.parse(value);
				/***************************/

				// ����id��������
				Sort sort = new Sort(new SortField("id", SortField.INT, true));

				TopDocs topDocs = indexSearcher.search(query, null, 1000, sort);

				// ScoreDoc �洢���ĵ����߼���ź��ĵ�����
				ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				for (ScoreDoc temp : scoreDocs) {
					// ����doc��idȥ��ȡdoc�ĵ� ,������Document
					Document doc = indexSearcher.doc(temp.doc);

					// ͨ�����������ùؼ��ָ���
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
			// ����ҵ��㷽�������������ӵ�ListView
			addBookToListView(books);
		}
	}

	private Context context = null;
	private ListView bookListView = null;
	private ArrayList<String> url = null;

	// ���������ѯ�����ĳ���
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
	 * @function:���鱾��Ϣ��ӵ�ListView
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param books
	 */
	public void addBookToListView(ArrayList<Book> books) {
		int count = books.size();
		if (count <= 0) {
			Toast.makeText(this.context, "û�з������ݣ�", Toast.LENGTH_SHORT).show();
			return;
		}
		// listItem���ڴ��books
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		// ÿ���鱾��Ϣ��Ӧ����ϸ��Ϣ����
		this.url = new ArrayList<String>();
		// ѭ������books������ֵ�Ե���ʽ����HashMap
		for (Book book : books) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", book.getName());
			item.put("firstTitle", book.getFirstTitle());
			item.put("secondTitle", book.getSecondTitle());
			item.put("content", book.getContent());
			url.add(HtmlUrlUtil.getUrl(book.getUrl()));
			// url.add(book.getUrl());

			// ��item������ݼӵ�listItem��
			listItem.add(item);
		}

		// �½��Զ���ViewAdapter
		ListViewAdapter listViewAdapter = new ListViewAdapter(this.context, listItem);

		// ��������װ�ص�listView��
		this.bookListView.setAdapter(listViewAdapter);

		// Ϊÿ��item��ע�����¼�
		this.bookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ���û����ĳ��item��ʱ�������������ҳ���鿴����ϸ����Ϣ
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
	 * @function:��ȡ��ѯ����
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param item
	 * @return String[]
	 */
	public String[] getIndexSearch(long item) {
		if (item == 0) {
			// ����������
			return TITLE;
		} else if (item == 1) {
			// ���²���
			return FIRSTTITLE;
		} else if (item == 2) {
			// ���ڲ���
			return SECONDTITLE;
		} else if (item == 3) {
			// �����ݲ���
			return CONTENT;
		} else {
			// ����ȫ��
			return ALL;
		}
	}

	/**
	 * 
	 * @function:��ѯ�鱾��Ϣ
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
