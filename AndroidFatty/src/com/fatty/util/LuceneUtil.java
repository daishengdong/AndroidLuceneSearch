package com.fatty.util;

import org.apache.lucene.search.IndexSearcher;

/**
 * 工具类，用于管理indexSearch
 */
public class LuceneUtil {
	private static IndexSearcher indexSearcher;

	/**
	 * 
	 * @function:使用的时候如果没有就创建一个新的IndexSearcher,同时要保证单例模式
	 * @author:Jerry
	 * @date:2013-9-24
	 * @return IndexSearcher
	 */
	public static IndexSearcher getIndexSearcher() {
		if (indexSearcher == null) {
			// 创建indexSearch,设置临界区
			synchronized (LuceneUtil.class) {
				if (indexSearcher == null) {
					try {
						indexSearcher = new IndexSearcher(LuceneConfigurationUtil.getDirectory());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return indexSearcher;
	}

	static {
		try {
			// 注册一个JVM关闭事件,关闭程序的时候执行
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					closeIndexSearcher();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @function:关闭IndexSearcher
	 * @author:Jerry
	 * @date:2013-9-24
	 */
	public static void closeIndexSearcher() {
		if (indexSearcher != null) {
			try {
				indexSearcher.close();
				indexSearcher = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
