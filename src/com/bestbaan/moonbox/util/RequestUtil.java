package com.bestbaan.moonbox.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class RequestUtil {

	Logger logger = Logger.getInstance();
	private static HttpClient client;
	private static RequestUtil util;

	private RequestUtil() {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 20000);
		HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
		SchemeRegistry localSchemeRegistry = new SchemeRegistry();
		localSchemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		localSchemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(
				basicHttpParams, localSchemeRegistry), basicHttpParams);
	}

	private HttpClient getHttpClient() {
		return client;
	}

	public static RequestUtil getInstance() {
		if (util == null)
			util = new RequestUtil();
		return util;
	}

	public String Post(String Url) {
		HttpPost localHttpGet = new HttpPost(Url);
		
		localHttpGet.setHeader("User-Agent", "cwhttp/v1.0");
		String str = null;
		try {
			str = EntityUtils.toString(getHttpClient().execute(localHttpGet)
					.getEntity(), "UTF-8");
			
		} catch (Exception e) {
		
			return "";
		}
		return str;
	}

	public String request(String param) {
		HttpGet localHttpGet = new HttpGet(param);

		localHttpGet.setHeader("User-Agent", "cwhttp/v1.0");
		String str = null;
		try {
			str = EntityUtils.toString(getHttpClient().execute(localHttpGet)
					.getEntity(), "UTF-8");
			logger.i("Request " + param + " return result [" + str + "]");
		} catch (Exception e) {
			logger.e("Request " + param + " error [ " + e.toString() + "]");
			return "";
		}
		return str;
	}
}