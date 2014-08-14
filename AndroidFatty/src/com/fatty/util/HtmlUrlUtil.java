package com.fatty.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工具类，用于读取properties文件的配置信息
 */
public class HtmlUrlUtil {
	private static Properties properties;
	static {
		InputStream is = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			is = classLoader.getResourceAsStream("properties/htmlUrl.properties");
			properties = new Properties();
			properties.load(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static String getUrl(String key) {
		return (String) properties.get(key);
	}

}
