package com.fatty.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import android.os.Environment;

/**
 * 工具类，用于管理Analyzer和Directory
 */
public class LuceneConfigurationUtil {
	private static Analyzer analyzer; // 分词器
	private static Directory directory;// 索引库

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static Directory getDirectory() {
		return directory;
	}

	static {
		// 新建一个分词器
		analyzer = new IKAnalyzer(true);
		try {
			// 获取索引库路径
			String path = Environment.getExternalStorageDirectory()
					+ File.separator + "fatty" + File.separator + "lucene_index";

			File targetDir = new File(path);
			// 打开索引库
			directory = FSDirectory.open(targetDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
