package com.pass.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONObject;

import com.lixd.jdbc.util.TextUtils;

public class RestfulClient {
	public static JSONObject queryQgVeh(String hpzl, String hphm) {
		DefaultHttpClient cl = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://10.36.6.7/forbid/services/forbid/queryVeh");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("hpzl", hpzl));
		params.add(new BasicNameValuePair("hphm", hphm));
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"utf-8");
			post.setEntity(entity);
			post.addHeader("Content-type", "application/json");
			HttpResponse rp = cl.execute(post);
			int st = rp.getStatusLine().getStatusCode();
			System.out.println(st);
			BufferedReader reader = new BufferedReader(new InputStreamReader(rp
					.getEntity().getContent(), "utf-8"));
			String result = "";
			String s = null;
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			JSONObject obj = new JSONObject(result);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject savePassInfo(String hpzl, String hphm,
			String cllx, String stDate, String endDate, String sjhm,
			String glbm, String syr) {
		DefaultHttpClient cl = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://10.36.6.7/forbid/services/forbid/savePassAtm");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("hpzl", hpzl));
		params.add(new BasicNameValuePair("hphm", hphm));
		params.add(new BasicNameValuePair("cllx", cllx));
		params.add(new BasicNameValuePair("stDate", stDate));
		params.add(new BasicNameValuePair("endDate", endDate));
		params.add(new BasicNameValuePair("sjhm", sjhm));
		params.add(new BasicNameValuePair("glbm", glbm));
		if (!TextUtils.isEmpty(syr))
			params.add(new BasicNameValuePair("syr", syr));
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"utf-8");
			post.setEntity(entity);
			post.addHeader("Content-type", "application/json");
			HttpResponse rp = cl.execute(post);
			int st = rp.getStatusLine().getStatusCode();
			System.out.println(st);
			BufferedReader reader = new BufferedReader(new InputStreamReader(rp
					.getEntity().getContent(), "utf-8"));
			String result = "";
			String s = null;
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			System.out.println("办证服务返回：" + result);
			JSONObject obj = new JSONObject(result);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String queryCloseArea(String points, String center,
			String callback) {
		DefaultHttpClient cl = new DefaultHttpClient();
		String url = "http://10.36.6.7/cross/services/cross/getCloseAreaCross";
		url += "?points=" + points + "&center=" + center + "&callback="
				+ callback;
		HttpGet get = new HttpGet(url);
		try {
			get.addHeader("Content-type", "application/json");
			HttpResponse rp = cl.execute(get);
			int st = rp.getStatusLine().getStatusCode();
			System.out.println(st);
			BufferedReader reader = new BufferedReader(new InputStreamReader(rp
					.getEntity().getContent(), "utf-8"));
			String result = "";
			String s = null;
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			// JSONObject obj = new JSONObject(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
