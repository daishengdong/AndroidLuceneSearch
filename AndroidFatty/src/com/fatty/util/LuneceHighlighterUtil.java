package com.fatty.util;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

/**
 * 工具类，用于设置字条高亮
 */
public class LuneceHighlighterUtil {

	private static Formatter formatter = null;
	private static final String STARTTAG = "<font color='red'>";
	private static final String ENDTAG = "</font>";
	private static final int SUBLENGTH = 10; // 要截取的字符串长度

	static {
		// Formatter： 设置高亮文本的样式
		formatter = new SimpleHTMLFormatter(STARTTAG, ENDTAG);
	}

	/**
	 * 
	 * @function:设置字体高亮
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param query
	 * @param text
	 * @param length
	 * @param value
	 * @return String
	 */
	public static String highlighterText(Query query, String text, int length,
			String value) {
		// 设置query对象,在query对象中,有查询的关键字
		Scorer scorer = new QueryScorer(query);

		Highlighter highlighter = new Highlighter(formatter, scorer);

		// 设置高亮后的字符长度
		highlighter.setTextFragmenter(new SimpleFragmenter(length));

		// 对某个field字段进行高亮 Analyzer:高亮前对字段进行分词
		// 如果在设置高亮的文本中,没有关键字,返回的是null,而不是原始数据
		String result = null;
		try {
			result = highlighter.getBestFragment(
					LuceneConfigurationUtil.getAnalyzer(), null, text);
			value = STARTTAG + value + ENDTAG;
			result = subString(text, length, result, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 
	 * @function:截取字符串
	 * @author:Jerry
	 * @date:2013-9-24
	 * @param text
	 * @param length
	 * @param result
	 * @param value
	 * @return String
	 */
	private static String subString(String text, int length, String result,
			String value) {

		String firstResult = null;
		String lastResult = null;
		if (result == null) {
			result = text;
			// 在设置高亮的文本中,没有关键字,显示前length个字符
			if (result.length() > length) {
				result = result.substring(0, length) + "...";
			}
		} else {
			if (result.length() < length) {
				// 在设置高亮的文本中,有关键字且长度小于规定长度，则不用截取
				return result;
			} else {
				// 在设置高亮的文本中,有关键字且长度大于规定长度，则截取第一个关键字出现前后的n个字符
				// 获取关键词前边的字符串的长度
				int firstIndex = result.indexOf(value);
				// 获取关键词的长度
				int valueLength = value.length();
				// 获取关键词后边的字符串的长度
				int lastIndex = result.length() - firstIndex - valueLength;

				// 如果关键词前边的字符串长度大于规定的长度，则 截取！
				if (firstIndex > SUBLENGTH) {
					firstResult = result.substring(firstIndex - SUBLENGTH,
							firstIndex);
					firstResult = "..." + firstResult;
				} else {
					firstResult = result.substring(0, firstIndex);
				}
				// 如果关键词后的字符串长度大于规定的长度，则 截取！
				if (lastIndex > SUBLENGTH) {
					lastResult = result.substring(firstIndex + valueLength,
							firstIndex + valueLength + SUBLENGTH);
					lastResult = lastResult + "...";
				} else {
					lastResult = result.substring(firstIndex + valueLength,
							result.length());

				}
				// 拼接截取的结果
				result = firstResult + value + lastResult;
			}

		}
		return result;
	}
}
