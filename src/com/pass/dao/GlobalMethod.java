package com.pass.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lixd.jdbc.util.TextUtils;

public class GlobalMethod {

	

	public static String getRequestParamer(HttpServletRequest request,
			String param) {
		String pa = request.getParameter(param);
		if (pa == null)
			pa = "";
		return pa.trim();
	}

	public static String getRequestParamer(HttpServletRequest request,
			String param, String defaultVal) {
		String pa = request.getParameter(param);
		if (pa == null)
			pa = defaultVal;
		return pa.trim();
	}

	/**
	 * 发送手机短信
	 * 
	 * @param text
	 * @param tel
	 * @return
	 */
	public static int sendSms(String text, String tel) {
		if (TextUtils.isEmpty(tel) || tel.length() != 11
				|| TextUtils.isEmpty(text))
			return -1;
		System.out.println(tel + "短信发送内容：" + text);
		String re = "";
		String url = "https://api.ums86.com:9600/sms/Api/Send.do";
		String conds = "SpCode=210148&LoginName=nt_jt&Password=xj2001&MessageContent="
				+ text;
		conds += "&UserNumber=" + tel + "&f=1";
		URL u;
		try {
			u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();
			out.write(conds.getBytes("gbk"));
			out.close();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "gbk"));
			String s = null;
			while ((s = reader.readLine()) != null) {
				re += s;
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("短信发送返回：" + re);
		return parseResult(re);
	}

	/**
	 * 发送手机短信
	 * 
	 * @param text
	 * @param tel
	 * @return
	 */
	public static String[] sendSmsDetail(String text, String tel) {
		if (TextUtils.isEmpty(tel) || tel.length() != 11
				|| TextUtils.isEmpty(text))
			return new String[] { "-1", "短信内容为空或手机号码不正确" };
		System.out.println(tel + "短信发送内容：" + text);
		String re = "";
		String url = "https://api.ums86.com:9600/sms/Api/Send.do";
		String conds = "SpCode=210148&LoginName=nt_jt&Password=xj2001&MessageContent="
				+ text;
		conds += "&UserNumber=" + tel + "&f=1";
		URL u;
		try {
			u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();
			out.write(conds.getBytes("gbk"));
			out.close();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "gbk"));
			String s = null;
			while ((s = reader.readLine()) != null) {
				re += s;
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("短信发送返回：" + re);
		return parseSms(re);
	}

	private static int parseResult(String re) {
		int smsResult = -1;
		String[] res = re.split("&");
		for (int i = 0; i < res.length; i++) {
			String s = res[i];
			int index = s.indexOf("=");
			if (index < 0)
				continue;
			if (s.startsWith("result")) {
				smsResult = Integer.valueOf(s.substring(index + 1));
			}
		}
		return smsResult;
	}

	private static String[] parseSms(String re) {
		String[] r = new String[2];
		int smsResult = -1;
		String[] res = re.split("&");
		for (int i = 0; i < res.length; i++) {
			String s = res[i];
			int index = s.indexOf("=");
			if (index < 0)
				continue;
			if (s.startsWith("result")) {
				r[0] = s.substring(index + 1);
			}
			if (s.startsWith("description")) {
				r[1] = s.substring(index + 1);
			}
		}
		return r;
	}

	public static String createRandomStr() {
		Random random = new Random();
		String s = String.valueOf(Math.abs(random.nextInt()) % 1000000);
		s = padLeftZero(s, 6);
		return s;
	}

	public static String createRandomStr(int len) {
		long ten = Math.round(Math.pow(10, len));
		Random random = new Random();
		String s = String.valueOf(Math.abs(random.nextInt()) % ten);
		s = padLeftZero(s, len);
		return s;
	}

	private static String padLeftZero(String s, int len) {
		for (int i = s.length(); i < len; i++) {
			s = "0" + s;
		}
		return s;
	}

	public static void writeMsg(HttpServletResponse response, String msg)
			throws IOException {
		PrintWriter out = response.getWriter();
		out.println(msg);
		out.flush();
		out.close();
	}

	public static void main(String[] args) {
		System.out.println(createRandomStr(8));
	}

}
