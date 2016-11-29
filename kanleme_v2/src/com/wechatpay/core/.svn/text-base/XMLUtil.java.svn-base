package com.wechatpay.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * xml工具类
 *
 * @author miklchen
 */
public class XMLUtil {

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 *
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map<String,String> doXMLParse(String strxml, String charset) throws JDOMException, IOException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"" + charset + "\"");

		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map<String,String> m = new HashMap<>();

		InputStream in = new ByteArrayInputStream(strxml.getBytes(charset));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List<?> list = root.getChildren();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List<?> children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = XMLUtil.getChildrenText(children);
			}

			m.put(k, v);
		}

		// 关闭流
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 *
	 * @param children
	 * @return String
	 */
	private static String getChildrenText(List<?> children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator<?> it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List<?> list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(XMLUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	public static Map<String, String> parseXml(HttpServletRequest request, String charset) throws Exception {
		// 解析结果存储在HashMap
		Map<String, String> map = new HashMap<>();
		InputStream inputStream = request.getInputStream();
		byte[] bytes = readInputStream(inputStream);
		closeIO(inputStream);
		if (bytes == null) {
			return map;
		}
		String res = new String(bytes, Charset.forName(charset));

		// 读取输入流

		org.dom4j.Document document = DocumentHelper.parseText(res);

		// 得到xml根元素
		org.dom4j.Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<org.dom4j.Element> elementList = root.elements();

		// 遍历所有子节点
		for (org.dom4j.Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		return map;
	}

	private static byte[] readInputStream(InputStream in) {
		byte[] buffer = new byte[10240];
		int len = -1;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			while (true) {
				len = in.read(buffer);
				if (len > 0) {
					out.write(buffer, 0, len);
				} else {
					break;
				}
			}
			out.flush();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeIO(out);
		}
		return null;
	}

	private static void closeIO(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
			io = null;
		}
	}
}