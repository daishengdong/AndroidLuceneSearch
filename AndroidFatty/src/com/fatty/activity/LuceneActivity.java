package com.fatty.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.fatty.action.LuceneAction;
import com.fatty.activity.R;

public class LuceneActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.law_layout);
		this.initView();
	}

	/**
	 * 
	 * @function:��ʼ��Activity����
	 * @author:Jerry
	 * @date:2013-9-24
	 */
	private void initView() {
		// ���������˵�
		Spinner indexSearch_sp = (Spinner) super
				.findViewById(R.id.index_search);
		indexSearch_sp.setPrompt("��ѡ���ѯ����");
		ArrayAdapter<CharSequence> item = ArrayAdapter.createFromResource(this,
				R.array.indexOfSearch, android.R.layout.simple_spinner_item);
		item.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		indexSearch_sp.setAdapter(item);

		// ��ȡ�����ؼ�
		EditText value_et = (EditText) super.findViewById(R.id.value);
		Button search_bt = (Button) super.findViewById(R.id.query_bt);
		ListView bookListView = (ListView) super.findViewById(R.id.content_lv);

		// �½��¼������࣬����ʵ��View.OnClickListener�ӿ�
		LuceneAction luceneAction = new LuceneAction(
				this.getApplicationContext(), indexSearch_sp, value_et,
				bookListView);

		// ��������ťע�����¼�
		search_bt.setOnClickListener(luceneAction);
	}
}