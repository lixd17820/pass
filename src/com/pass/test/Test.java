package com.pass.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Test {

	public static JSONObject getPassInfo() throws Exception {
		// String s =
		// "{\"_id\":33,\"hpzl\":\"01\",\"hphm\":\"苏F33106\",\"stDate\":\"2017-01-01 00:00:00\",\"endDate\":\"2017-01-02 00:00:00\",\"zqmj\":\"061247\",\"gxsj\":\"2017-01-26 16:24:05\",\"passName\":\"普通货车禁区通行证\",\"passTitle\":\"普通货车禁区通行证\",\"passAge\":0,\"isRepeat\":0,\"checkRequire\":0,\"isAtm\":1,\"yxbj\":1,\"spbj\":0,\"isFirst\":1,\"areas\":[{\"areaName\":\"市区外围禁区\",\"jxsj\":[{\"stime\":7,\"etime\":19}],\"stLines\":[],\"stPoints\":[],\"stOut\":[],\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}],\"forbid\":{},\"cross\":[]},{\"areaName\":\"开发区禁区\",\"jxsj\":[{\"stime\":7,\"etime\":19}],\"stLines\":[],\"stPoints\":[],\"stOut\":[],\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]}]}";
		// System.out.println(s.substring(587));
		File f = new File("f:/pass_info.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		String s = null;
		String re = "";
		while ((s = br.readLine()) != null) {
			re += s;
		}
		br.close();
		JSONObject obj = new JSONObject(re);
		System.out.println(obj);
		return obj;

	}

}
