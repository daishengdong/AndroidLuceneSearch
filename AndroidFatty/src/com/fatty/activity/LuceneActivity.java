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
	 * @function:初始化Activity界面
	 * @author:Jerry
	 * @date:2013-9-24
	 */
	private void initView() {
		// 设置下拉菜单
		Spinner indexSearch_sp = (Spinner) super
				.findViewById(R.id.index_search);
		indexSearch_sp.setPrompt("请选择查询条件");
		ArrayAdapter<CharSequence> item = ArrayAdapter.createFromResource(this,
				R.array.indexOfSearch, android.R.layout.simple_spinner_item);
		item.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		indexSearch_sp.setAdapter(item);

		// 获取各个控件
		EditText value_et = (EditText) super.findViewById(R.id.value);
		Button search_bt = (Button) super.findViewById(R.id.query_bt);
		ListView bookListView = (ListView) super.findViewById(R.id.content_lv);

		// 新建事件处理类，该类实现View.OnClickListener接口
		LuceneAction luceneAction = new LuceneAction(
				this.getApplicationContext(), indexSearch_sp, value_et,
				bookListView);

		// 给搜索按钮注册点击事件
		search_bt.setOnClickListener(luceneAction);
	}
}