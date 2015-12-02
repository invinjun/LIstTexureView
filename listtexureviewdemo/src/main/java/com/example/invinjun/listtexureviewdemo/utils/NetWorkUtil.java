package com.example.invinjun.listtexureviewdemo.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 
 * @author Mathew
 * 
 */
public class NetWorkUtil {
	private static long status;
//	private static BasicHeader[] headers = new BasicHeader[10];
//	static {
//		headers[0] = new BasicHeader("Appkey", "");
		
//	}

	/*
	 * 
	 */
//	public static String post(String url, HashMap<String, String> requestDataMap) {
//		DefaultHttpClient client = new DefaultHttpClient();
//
//		HttpPost post = new HttpPost(url);
//		HttpParams params = new BasicHttpParams();//
//		params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params, 3000);
//		HttpConnectionParams.setSoTimeout(params, 3000);
//		post.setParams(params);
//		//设置请求头
//		// post.setHeaders(headers);
//		Object obj = null;
//		try {
//			if (requestDataMap != null) {
//				HashMap<String, String> map = requestDataMap;
//				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
//				for (Map.Entry<String, String> entry : map.entrySet()) {
//					BasicNameValuePair pair = new BasicNameValuePair(
//							entry.getKey(), entry.getValue());
//					pairList.add(pair);
//				}
//				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
//				post.setEntity(entity);
//			}
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				String result = EntityUtils.toString(response.getEntity(),
//						"UTF-8");
//
//				return result;
//			}
//		} catch (ClientProtocolException e) {
//		} catch (IOException e) {
//		}
//		return null;
//	}

//	/**
//	 *
//	 * @param vo
//	 * @return
//	 */
//	public static String get(String url) {
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet get = new HttpGet(url);
//		HttpParams params = new BasicHttpParams();//
//		params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params, 5000);
//		HttpConnectionParams.setSoTimeout(params, 5000);
//		get.setParams(params);
//		// get.setHeaders(headers);
//		Object obj = null;
//		try {
//			HttpResponse response = client.execute(get);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				String result = EntityUtils.toString(response.getEntity(),
//						"UTF-8");
//				return result;
//
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "null";
//	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context,"网络不可用，请重新设置", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}
	public static boolean isAvailable(final Context pContext) {
		final ConnectivityManager conManager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isWifiState(final Context pContext) {
		ConnectivityManager mConnectivity = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		int netType = -1;
		if (info != null && info.isConnected()) {
			netType = info.getType();
			if (netType == ConnectivityManager.TYPE_WIFI)
				return info.isConnected();
		}
		return false;
	}

	public static boolean isGPRSState(final Context pContext) {
		ConnectivityManager mConnectivity = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		int netType = -1;
		if (info != null && info.isConnected()) {
			netType = info.getType();
			if (netType == ConnectivityManager.TYPE_MOBILE)
				return info.isConnected();
		}
		return false;
	}
	public static long getStatus() {

		new Thread() {
			public void run() {
				try {
					URL url = new URL("http://www.bjtime.cn");
					URLConnection uc = url.openConnection();
					uc.connect();
					status = uc.getDate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		return status;
	}
}
