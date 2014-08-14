package com.fatty.action;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.fatty.po.Book;
import com.fatty.service.LuceneService;

public class LuceneAction implements OnClickListener {

	private Context context = null;
	private Spinner indexSearch_sp = null;
	private EditText value_et = null;
	private ListView bookListView = null;
	LuceneService luceneService = null;

	public LuceneAction(Context context, Spinner indexSearch_sp,
			EditText value_et, ListView bookListView) {
		super();
		this.context = context;
		this.indexSearch_sp = indexSearch_sp;
		this.value_et = value_et;
		this.bookListView = bookListView;

		// �½�ҵ������
		this.luceneService = new LuceneService(this.context, this.bookListView);
	}

	@Override
	public void onClick(View v) {
		// ��ȡ��ѯ����
		long itemId = this.indexSearch_sp.getSelectedItemId();
		String[] indexSearch = this.luceneService.getIndexSearch(itemId);

		// ��ȡ��ѯ����
		String value = this.value_et.getText().toString().trim();

		// ����ҵ��㷽�����в�ѯ
		ArrayList<Book> books = this.luceneService.queryBook(indexSearch, value);

		// ����ҵ��㷽�������������ӵ�ListView
		this.luceneService.addBookToListView(books);

		/*
		Intent intent = new Intent();
		intent.setClass(this.context, ResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.context.startActivity(intent);
		*/
	}

}
