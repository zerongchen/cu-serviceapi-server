package com.aotain.serviceapi.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	public static final String UTF8 = "UTF-8";  // 定义编码格式 UTF-8
	public static final String GBK = "GBK";  // 定义编码格式 GBK
	private static final String EMPTY = "";

	/**
	 * post请求
	 * 
	 * @param url
	 * @param formparams
	 * @param encodeCharset
	 * @return
	 * @throws IOException
	 */
	public static String getRequest(String url, String encodeCharset)
			throws IOException {
		String postContent = EMPTY;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpGet httpget = new HttpGet(url);
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					postContent = EntityUtils.toString(entity, encodeCharset);
				}
			} finally {
				response.close();
			}
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				LogFactory.getLog(HttpUtils.class).error("get请求失败", e);
			}
		}
		return postContent;

	}
	
	/**
	 * post请求
	 * 
	 * @param url
	 * @param formparams
	 * @param encodeCharset
	 * @return
	 * @throws IOException
	 */
	public static String postRequest(String url, List<NameValuePair> formparams, String encodeCharset)
			throws IOException {
		String postContent = EMPTY;
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, encodeCharset);
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					postContent = EntityUtils.toString(entity, encodeCharset);
				}
			} finally {
				response.close();
			}
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				LogFactory.getLog(HttpUtils.class).error("post请求失败", e);
			}
		}
		return postContent;

	}

	/**
	 * http post请求
	 * @param path
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static String postRequest(String path, String body,String charset) throws Exception {
		URL url = null;
		url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		// 获取URLConnection对象对应的输出流
		PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		printWriter.write(body);// post的参数 xx=xx&yy=yy
		// flush输出流的缓冲
		printWriter.flush();
		// 开始获取数据
		StringBuffer rBuffer = new StringBuffer();
		String lines;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		while ((lines = reader.readLine()) != null) {
			if(rBuffer.length() > 0){
				rBuffer.append("\r\n");
			}
			rBuffer.append(lines);
		}
		return rBuffer.toString();
	}
}
