package com.pass.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bson.Document;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.lixd.jdbc.util.TextUtils;

public class DaoUtils {

	public static JSONArray docsToJsonArray(ArrayList<Document> f) {
		JSONArray array = new JSONArray();
		for (Document doc : f) {
			JSONObject obj = new JSONObject();
			doc2Json(doc, obj);
			array.put(obj);
		}
		return array;
	}

	public static JSONArray docsToJsonArray(ArrayList<Document> f,
			String... params) {
		JSONArray array = new JSONArray();
		if (params == null || params.length == 0 || params.length % 2 == 1)
			return array;
		int len = params.length / 2;
		for (Document doc : f) {
			try {
				JSONObject obj = new JSONObject();
				for (int i = 0; i < len; i++) {
					String sn = params[i * 2];
					String tn = params[i * 2 + 1];
					String val = doc.getString(sn);
					if (!TextUtils.isEmpty(val))
						obj.put(tn, val);
				}
				array.put(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	/**
	 * 灏咼SON瀵硅薄杞崲涓烘枃妗�
	 * 
	 * @param obj
	 * @return
	 */
	public static Document jsonObj2Doc(JSONObject obj) {
		if (obj == null)
			return null;
		Document d = new Document();
		try {
			Iterator<String> keys = obj.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				Object val = obj.opt(key);
				if ("org.codehaus.jettison.json.JSONArray".equals(val
						.getClass().getName())) {
					JSONArray array = (JSONArray) val;
					List list = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						Object s = array.get(i);
						list.add(s);
					}
					d.put(key, list);
				} else {
					d.put(key, val);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	public static JSONArray list2Array(List<Document> f) {
		JSONArray array = new JSONArray();
		for (Document doc : f) {
			JSONObject json = new JSONObject();
			doc2Json(doc, json);
			array.put(json);
		}
		return array;
	}

	public static void docs2JsonArray(List<Document> docs, JSONArray array) {
		for (Document doc : docs) {
			JSONObject json = new JSONObject();
			doc2Json(doc, json);
			array.put(json);
		}
	}

	public static void doc2Json(Document doc, JSONObject json) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Set<Entry<String, Object>> set = doc.entrySet();
		for (Entry<String, Object> entry : set) {
			try {
				String key = entry.getKey();
				Object obj = doc.get(key);
				// System.out.println(key + ":" + obj.getClass().getName());
				if (obj == null)
					continue;
				if (obj instanceof java.util.ArrayList) {
					ArrayList docs = (ArrayList) obj;
					JSONArray array = new JSONArray();
					if (docs != null && !docs.isEmpty()) {
						if (docs.get(0) instanceof Document) {
							ArrayList<Document> ds = (ArrayList<Document>) docs;
							for (Document d : ds) {
								JSONObject j = new JSONObject();
								doc2Json(d, j);
								array.put(j);
							}
							json.put(key, array);
						} else if (docs.get(0) instanceof java.lang.String) {
							ArrayList<String> ds = (ArrayList<String>) docs;
							json.put(key, ds);
						}else if (docs.get(0) instanceof java.lang.Integer) {
							ArrayList<Integer> ds = (ArrayList<Integer>) docs;
							json.put(key, ds);
						}else if (docs.get(0) instanceof java.lang.Long) {
							ArrayList<Long> ds = (ArrayList<Long>) docs;
							json.put(key, ds);
						}
					}
				} else if (obj instanceof org.bson.Document) {
					JSONObject j = new JSONObject();
					Document temp = (Document) obj;
					doc2Json(temp, j);
					json.put(key, j);
				} else if (obj instanceof java.util.Date) {
					json.put(key, sdf.format((Date) obj));
				} else {
					if ("undefined".equals(obj))
						continue;
					json.put(key, obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static JSONArray getJsonArrayByMap(Map<String, String> map) {
		JSONArray ar = new JSONArray();
		if (map == null)
			return ar;
		for (Entry<String, String> entry : map.entrySet()) {
			JSONObject temp = new JSONObject();
			try {
				temp.put("key", entry.getKey());
				temp.put("value", entry.getValue());
				ar.put(temp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ar;
	}

	public static JSONArray getJsonArrayByArray(JSONArray ar, String key,
			String value, boolean isInt) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < ar.length(); i++) {
			JSONObject obj = ar.optJSONObject(i);
			JSONObject temp = new JSONObject();
			try {
				temp.put("key", isInt ? String.valueOf(obj.getInt(key)) : obj
						.getString(key));
				temp.put("value", obj.optString(value));
				array.put(temp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	public static Map<String, String> megerMap(Map<String, String> m1,
			Map<String, String> m2) {
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, String> entry : m1.entrySet()) {
			if (m2.containsKey(entry.getKey())) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	public static List<String> getListFromMap(Map<String, String> m1) {
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : m1.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}

	public static List<String> megerListMap(List<String> m1, JSONArray strs) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strs.length(); i++) {
			String s = strs.optString(i);
			if (m1.contains(s)) {
				list.add(s);
			}
		}
		return list;
	}
}
