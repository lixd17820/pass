package com.pass.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.lixd.jdbc.util.JdbcUtils;
import com.lixd.jdbc.util.TextUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

public class ForbidMongoDao {

	public static final String role = "role";
	public static String forbidDb = "forbid";
	public static String stForbidTb = "st_forbid";
	public static String areaSt = "area_st";
	public static String areaSub = "area_sub";
	public static String passInfo = "pass_info";
	public static String passCatalog = "pass_catalog";
	public static String passMb = "pass_mb";
	public static String timeMb = "time_mb";

	public static MongoCollection<Document> getCol(String tb) {
		return MongoDBUtil.instance.getCollection(forbidDb, tb);
	}

	public static long getSeq(String tableName) {
		MongoCollection<Document> col = getCol("sequence");
		Document d = col.findOneAndUpdate(Filters.eq("coll_name", tableName),
				Updates.inc("cnt", 1L));
		if (d == null) {
			col.insertOne(new Document("coll_name", tableName)
					.append("cnt", 1L));
			return 1;
		}
		return d.getLong("cnt");
	}

	public static List<String> getList(String[] array) {
		List<String> list = new ArrayList<String>();
		for (String s : array) {
			list.add(s);
		}
		return list;
	}

	/**
	 * 缁煎悎鏌ヨ鏍囧噯绂佸尯
	 * 
	 * @param conds
	 * @return
	 */
	public static JSONArray queryStForbid(Bson conds) {
		MongoCollection<Document> col = getCol(stForbidTb);
		FindIterable<Document> temp = null;
		if (conds == null)
			temp = col.find();
		else
			temp = col.find(conds);
		ArrayList<Document> docs = temp.into(new ArrayList<Document>());
		if (docs == null || docs.isEmpty())
			return null;
		JSONArray ar = DaoUtils.docsToJsonArray(docs);
		return ar;
	}

	/**
	 * 鏍规嵁ID鏌ヨ鏍囧噯鍖哄煙
	 * 
	 * @param id
	 * @return
	 */
	public static JSONObject queryStForbidById(String id) {
		MongoCollection<Document> col = getCol(stForbidTb);
		Document doc = col.find(Filters.eq("_id", Integer.valueOf(id))).first();
		JSONObject obj = new JSONObject();
		DaoUtils.doc2Json(doc, obj);
		return obj;
	}

	/**
	 * 鏌ヨ鏍囧噯鍖哄煙
	 * 
	 * @param conds
	 * @return
	 */
	public static JSONArray queryArea(List<Bson> conds, String tb) {
		MongoCollection<Document> col = getCol(tb);
		FindIterable<Document> temp = null;
		if (conds == null || conds.isEmpty())
			temp = col.find();
		else
			temp = col.find(Filters.and(conds));
		ArrayList<Document> docs = temp.into(new ArrayList<Document>());
		if (docs == null || docs.isEmpty())
			return null;
		JSONArray ar = DaoUtils.docsToJsonArray(docs);
		return ar;
	}

	public static JSONArray queryArea(List<Bson> conds, String tb, String order) {
		MongoCollection<Document> col = getCol(tb);
		FindIterable<Document> temp = null;
		if (conds == null || conds.isEmpty())
			temp = col.find().sort(Sorts.descending(order));
		else
			temp = col.find(Filters.and(conds)).sort(Sorts.descending(order));
		ArrayList<Document> docs = temp.into(new ArrayList<Document>());
		if (docs == null || docs.isEmpty())
			return null;
		System.out.println(docs.toString());
		JSONArray ar = DaoUtils.docsToJsonArray(docs);
		return ar;
	}

	/**
	 * 鍏敤鏌ヨ鏂规硶
	 * 
	 * @param conds
	 * @param tb
	 * @return
	 */
	public static JSONArray queryComm(List<Bson> conds, String tb) {
		MongoCollection<Document> col = getCol(tb);
		FindIterable<Document> temp = null;
		if (conds == null || conds.isEmpty())
			temp = col.find();
		else
			temp = col.find(Filters.and(conds));
		ArrayList<Document> docs = temp.into(new ArrayList<Document>());
		if (docs == null || docs.isEmpty())
			return null;
		JSONArray ar = DaoUtils.docsToJsonArray(docs);
		return ar;
	}

	public static int updateStForbidText(String id, String name, String hpzls,
			String cllxs, String minHdzzl, String maxHdzzl) {
		MongoCollection<Document> col = getCol(stForbidTb);
		Bson cond = Filters.eq("_id", Integer.valueOf(id));
		Document doc = col.find(cond).first();
		doc.put("stForbidName", name);
		doc.put("hpzl", getList(hpzls.split(",")));
		doc.put("cllx", getList(cllxs.split(",")));
		if (!TextUtils.isEmpty(minHdzzl))
			doc.put("minHdzzl", Integer.valueOf(minHdzzl));
		else {
			if (doc.getInteger("minHdzzl") != null)
				doc.remove("minHdzzl");
		}
		if (!TextUtils.isEmpty(maxHdzzl))
			doc.put("maxHdzzl", Integer.valueOf(maxHdzzl));
		else {
			if (doc.getInteger("maxHdzzl") != null)
				doc.remove("maxHdzzl");
		}
		doc.put("gxsj", new Date());
		Document d = col.findOneAndReplace(cond, doc);
		return d == null ? 0 : 1;
	}

	public static int addStForbidText(String stForbidName, String hpzls,
			String cllxs, String minHdzzl, String maxHdzzl) {
		MongoCollection<Document> col = getCol(stForbidTb);
		Document doc = new Document();
		long id = getSeq(stForbidTb);
		doc.put("_id", (int) id);
		doc.put("stForbidName", stForbidName);
		doc.put("hpzl", getList(hpzls.split(",")));
		doc.put("cllx", getList(cllxs.split(",")));
		if (!TextUtils.isEmpty(minHdzzl))
			doc.put("minHdzzl", Integer.valueOf(minHdzzl));
		if (!TextUtils.isEmpty(maxHdzzl))
			doc.put("maxHdzzl", Integer.valueOf(maxHdzzl));
		doc.put("gxsj", new Date());
		col.insertOne(doc);
		return doc == null ? 0 : 1;
	}

	/**
	 * 淇濆瓨鏍囧噯鍖哄煙
	 * 
	 * @param name
	 * @param area
	 * @param exclude
	 * @param isClose
	 * @param center
	 * @param wd
	 * @param jd
	 * @return
	 */
	public static long saveAreaSt(String name, String dlmc, String area,
			String exclude, String isClose, String center, String jd, String wd) {
		MongoCollection<Document> col = getCol(areaSt);
		long id = getSeq(areaSt);
		Document doc = new Document();
		doc.put("_id", (int) id);
		doc.put("name", name);
		doc.put("dlmc", dlmc);
		doc.put("isClose", TextUtils.isEmpty(isClose) ? 0 : Integer
				.valueOf(isClose));
		if (!TextUtils.isEmpty(center)) {
			Document d = new Document();
			d.put("crossId", center);
			d.put("jd", jd);
			d.put("wd", wd);
			doc.put("center", d);
		}
		if (!TextUtils.isEmpty(area))
			doc.put("area", getList(area.split(",")));
		if (!TextUtils.isEmpty(exclude))
			doc.put("out", getList(exclude.split(",")));
		doc.put("gxsj", new Date());
		col.insertOne(doc);
		return doc == null ? 0 : id;
	}

	public static int delArea(String id, String catalog) {
		String tb = "";
		if (TextUtils.equals(catalog, "st"))
			tb = areaSt;
		else if (TextUtils.equals(catalog, "sub"))
			tb = areaSub;
		if (TextUtils.isEmpty(tb))
			return 0;
		MongoCollection<Document> col = getCol(tb);
		Bson cond = Filters.eq("_id", Integer.valueOf(id));
		Document doc = col.find(cond).first();
		if (doc == null)
			return -1;
		doc.put("scbj", 1);
		Document d = col.findOneAndReplace(cond, doc);
		return d == null ? 0 : 1;
	}

	public static long saveAreaSub(String areaStId, String name, String area,
			String exclude, String isClose, String center, String jd,
			String wd, String subCatalog, String dlmc) {
		MongoCollection<Document> col = getCol(areaSub);
		long id = getSeq(areaSub);
		Document doc = new Document();
		doc.put("_id", (int) id);
		doc.put("stId", Integer.valueOf(areaStId));

		doc.put("name", name);
		doc.put("isClose", TextUtils.isEmpty(isClose) ? 0 : Integer
				.valueOf(isClose));
		if (!TextUtils.isEmpty(center)) {
			Document d = new Document();
			d.put("crossId", center);
			d.put("jd", jd);
			d.put("wd", wd);
			doc.put("center", d);
		}
		if (!TextUtils.isEmpty(area))
			doc.put("area", getList(area.split(",")));
		if (!TextUtils.isEmpty(exclude))
			doc.put("out", getList(exclude.split(",")));
		if (!TextUtils.isEmpty(subCatalog))
			doc.put("subCatalog", subCatalog);
		if (!TextUtils.isEmpty(dlmc))
			doc.put("dlmc", dlmc);
		doc.put("gxsj", new Date());
		col.insertOne(doc);
		return doc == null ? 0 : id;
	}

	public static int updateForbidAreaTime(String id, String areaId,
			String areaName, String jxsj) throws JSONException {
		MongoCollection<Document> col = getCol(stForbidTb);
		Bson cond = Filters.eq("_id", Integer.valueOf(id));
		Document doc = col.find(cond).first();
		if (doc == null)
			return 0;
		JSONArray jxs = new JSONArray(jxsj);
		if (jxs == null)
			return -1;
		List<Document> sjDocs = new ArrayList<Document>();
		for (int i = 0; i < jxs.length(); i++) {
			JSONObject obj = jxs.getJSONObject(i);
			Document sj = new Document();
			sj.put("stime", obj.getDouble("stime"));
			sj.put("etime", obj.getDouble("etime"));
			sjDocs.add(sj);
		}
		Document at = new Document();
		at.put("areaId", areaId);
		at.put("areaName", areaName);
		at.put("jxsj", sjDocs);
		doc.put("areaTime", at);
		Document d = col.findOneAndReplace(cond, doc);
		return d == null ? 0 : 1;
	}

	public static Document queryPassCataById(String cataId) {
		Document pc = getCol(passCatalog).find(
				Filters.eq("_id", Long.valueOf(cataId))).first();
		return pc;
	}

	public static long savePassInfo(String hpzl, String hphm, Date stDate,
			Date endDate, JSONArray areas, String zqmj, String isFirst,
			Document passCatalog) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MongoCollection<Document> col = getCol(passInfo);
		long id = getSeq(passInfo);
		int isf = TextUtils.isEmpty(isFirst) ? 0 : Integer.valueOf(isFirst);
		Document doc = new Document();
		doc.put("_id", id);
		doc.put("hpzl", hpzl);
		doc.put("hphm", hphm);
		doc.put("stDate", stDate);
		doc.put("endDate", endDate);
		doc.put("zqmj", zqmj);
		doc.put("gxsj", new Date());
		doc.put("passCatalog", passCatalog);
		if (isf > 0 && passCatalog.getInteger("checkRequire") > 0) {
			doc.put("yxbj", 0);
		} else {
			doc.put("yxbj", 1);
		}
		doc.put("spbj", 0);
		doc.put("isFirst", isf);
		System.out.println(doc.toJson());
		List<Document> list = new ArrayList<Document>();
		for (int i = 0; i < areas.length(); i++) {
			Document temp = new Document();
			JSONObject obj = areas.optJSONObject(i);
			// 鏍囧噯绂佸尯
			temp.put("stForbid", obj.optString("stForbid"));
			// 瀛愬尯绫诲瀷
			temp.put("subCatalog", obj.optString("subCatalog"));
			// 椤圭洰鍚嶇О
			if (!TextUtils.isEmpty(obj.optString("xmmc")))
				temp.put("xmmc", obj.optString("xmmc"));
			// 椤圭洰鏈夋晥鏈�
			Date yxqz = null;
			try {
				if (!TextUtils.isEmpty(obj.optString("yxqz")))
					yxqz = sdf.parse(obj.optString("yxqz"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (yxqz != null)
				temp.put("yxqz", yxqz);
			// 鍑嗚鏃堕棿
			JSONArray zxsjs = obj.optJSONArray("zxsj");
			if (zxsjs != null && zxsjs.length() > 0) {
				temp.put("zxsj", getJxsjList(zxsjs));
			}
			// 璺锛屾牴鎹畇ubCatalog鏉ュ喅瀹氬噯琛屾垨绂佽
			JSONArray cross = obj.optJSONArray("cross");
			if (cross != null && cross.length() > 0) {
				List<Document> cDoc = new ArrayList<Document>();
				for (int j = 0; j < cross.length(); j++) {
					Document d = new Document();
					JSONObject c = cross.optJSONObject(j);
					d.put("road", c.optString("road"));
					d.put("roadName", c.optString("roadName"));
					d.put("st", c.optString("st"));
					d.put("stName", c.optString("stName"));
					d.put("end", c.optString("end"));
					d.put("endName", c.optString("endName"));
					cDoc.add(d);
				}
				temp.put("cross", cDoc);
			}
			JSONObject forbid = obj.optJSONObject("forbid");
			if(forbid != null){
				Document fdoc = new Document();
				fdoc.put("stId", forbid.optInt("stId"));
				fdoc.put("subId", forbid.optInt("_id"));
				fdoc.put("subName", forbid.optString("name"));
				fdoc.put("roadName", forbid.optString("dlmc"));
				temp.put("forbid", fdoc);
			}
			list.add(temp);
		}
		doc.put("areas", list);
		System.out.println(doc.toJson());
		col.insertOne(doc);
		return id;
	}

	public static long saveRepeatPass(Document doc, Date st, Date et,
			String jybh, String jyxm) {
		MongoCollection<Document> col = getCol(passInfo);
		long id = getSeq(passInfo);
		doc.put("_id", id);
		doc.put("stDate", st);
		doc.put("endDate", et);
		doc.put("gxsj", new Date());
		doc.put("isFirst", 0);
		doc.put("yxbj", 1);
		doc.put("spbj", 0);
		doc.put("zqmj", jybh);
		doc.put("jyxm", jyxm);
		col.insertOne(doc);
		return id;
	}

	private static List<Document> getJxsjList(JSONArray ar) {
		List<Document> jxsjs = new ArrayList<Document>();
		if (ar == null)
			return jxsjs;
		for (int j = 0; j < ar.length(); j++) {
			JSONObject o = ar.optJSONObject(j);
			Document jxsj = new Document();
			jxsj.put("stime", o.optDouble("stime"));
			jxsj.put("etime", o.optDouble("etime"));
			jxsjs.add(jxsj);
		}
		return jxsjs;
	}

	private static List<String> getStrListByArray(JSONArray ar) {
		List<String> jxsjs = new ArrayList<String>();
		if (ar == null || ar.length() == 0)
			return jxsjs;
		for (int j = 0; j < ar.length(); j++) {
			try {
				jxsjs.add(ar.getString(j));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jxsjs;
	}

	private static List<Integer> getIntListByArray(JSONArray ar) {
		List<Integer> jxsjs = new ArrayList<Integer>();
		if (ar == null || ar.length() == 0)
			return jxsjs;
		for (int j = 0; j < ar.length(); j++) {
			try {
				int q = Integer.valueOf(ar.getString(j));
				System.out.println(q);
				jxsjs.add(q);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jxsjs;
	}

	public static JSONObject queryPassInfoById(String id) {
		Document doc = queryPassDocById(id);
		if (doc == null)
			return null;
		JSONObject pass = new JSONObject();
		DaoUtils.doc2Json(doc, pass);
		changePassDetail(pass);
		return pass;
	}

	public static Document queryPassDocById(String id) {
		Document doc = getCol(passInfo).find(
				Filters.eq("_id", Long.valueOf(id))).first();
		if (doc == null)
			return null;
		return doc;
	}

	public static void changePassDetail(JSONObject pass) {
		try {
			pass.put("isFirstName", GlobalData.isFirstMap.get(pass
					.optString("isFirst")));
			pass
					.put("yxbjName", GlobalData.yxbjMap.get(pass
							.optString("yxbj")));
			pass
					.put("spbjName", GlobalData.spbjMap.get(pass
							.optString("spbj")));
			JSONArray areas = pass.optJSONArray("areas");
			for (int i = 0; i < areas.length(); i++) {
				JSONObject area = areas.optJSONObject(i);
				String stId = area.optString("stForbid");
				area.put("stForbidObj", ForbidMongoDao.queryStForbidById(stId));
				area.put("subCatalogName", GlobalData.subCatalogMap.get(area
						.optString("subCatalog")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkPassInfo(String hpzl, String hphm, Date st,
			Date et, int age) {
		Bson conds = Filters.and(Filters.eq("hpzl", hpzl), Filters.eq("hphm",
				hphm), Filters.eq("passCatalog.passAge", age), Filters.or(
				Filters.and(Filters.lte("stDate", st), Filters.gte("endDate",
						st)), Filters.and(Filters.lte("stDate", et), Filters
						.gte("endDate", et))));
		MongoCollection<Document> col = getCol(passInfo);
		return col.find(conds).iterator().hasNext();
	}

	public static int saveOrModPassCatalog(JSONObject pc) {
		int row = 0;
		MongoCollection<Document> col = getCol(passCatalog);
		Document doc = new Document();
		boolean isMod = false;
		String id = pc.optString("id");
		if (!TextUtils.isEmpty(id)) {
			doc = col.find(Filters.eq("_id", Long.valueOf(id))).first();
			isMod = true;
		} else {
			long lid = getSeq(passCatalog);
			doc.put("_id", lid);
		}
		doc.put("passName", pc.optString("passName"));
		doc.put("passAge", pc.optInt("passAge", 0));
		doc.put("passTitle", pc.optString("passTitle"));
		doc.put("isRepeat", pc.optInt("isRepeat", 0));
		doc.put("checkRequire", pc.optInt("checkRequire", 0));
		doc.put("defaultTime", pc.optInt("defaultTime", 0));
		doc.put("maxTime", pc.optInt("maxTime", 0));
		doc.put("timeUnit", pc.optInt("timeUnit", 0));
		doc.put("isAtm", pc.optInt("isAtm", 0));
		doc.put("passMemo", pc.optString("passMemo"));
		doc.put("gxsj", new Date());
		if (isMod) {
			Document d = col.findOneAndReplace(Filters.eq("_id", Long
					.valueOf(id)), doc);
			row = d != null ? 1 : 0;
		} else {
			doc.put("yxbj", "0");
			col.insertOne(doc);
			row = 1;
		}
		return row;
	}

	public static int modPassCatalogHp(long id, String hpzls, String cllxs) {
		int row = 0;
		Bson cond = Filters.eq("_id", Long.valueOf(id));
		MongoCollection<Document> col = getCol(passCatalog);
		Document doc = col.find(cond).first();
		doc.put("hpzl", getList(hpzls.split(",")));
		doc.put("cllx", getList(cllxs.split(",")));
		doc.put("gxsj", new Date());
		doc.put("yxbj", "1");
		Document d = col.findOneAndReplace(cond, doc);
		row = d != null ? 1 : 0;
		return row;
	}

	public static JSONObject queryPassCatalogById(String id) {
		MongoCollection<Document> col = getCol(passCatalog);
		Document doc = col.find(Filters.eq("_id", Long.valueOf(id))).first();
		JSONObject obj = new JSONObject();
		DaoUtils.doc2Json(doc, obj);
		try {
			obj.put("passAgeName", GlobalData.passAgeMap.get(obj
					.optInt("passAge")
					+ ""));
			obj.put("isRepeatName", GlobalData.repeatMap.get(obj
					.optInt("isRepeat")
					+ ""));
			obj.put("checkRequireName", GlobalData.checkReqMap.get(obj
					.optInt("checkRequire")));
			obj.put("timeUnitName", GlobalData.timeUnitMap.get(obj
					.optInt("timeUnit")
					+ ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static int savePassMb(String passId, String mbName, String mbMemo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int row = 0;
		JSONObject pass = queryPassInfoById(passId);
		MongoCollection<Document> col = getCol(passMb);
		Document doc = new Document();
		doc.put("_id", (int) getSeq(passMb));
		doc.put("mbName", mbName);
		doc.put("mbMemo", mbMemo);
		JSONArray areas = pass.optJSONArray("areas");
		List<Document> docs = new ArrayList<Document>();
		List<String> hpzlList = DaoUtils.getListFromMap(GlobalData.hpzlMap);
		List<String> cllxList = DaoUtils.getListFromMap(GlobalData.cllxMap);
		for (int i = 0; i < areas.length(); i++) {
			JSONObject area = areas.optJSONObject(i);
			Document temp = new Document();
			temp.put("stForbid", area.optString("stForbid"));
			temp.put("subCatalog", area.optString("subCatalog"));
			temp.put("xmmc", area.optString("xmmc"));
			String yxqz = area.optString("yxqz");
			if (!TextUtils.isEmpty(yxqz)) {
				try {
					temp.put("yxqz", sdf.parse(yxqz));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			List<Document> zxsjDocs = new ArrayList<Document>();
			JSONArray zxsjs = area.optJSONArray("zxsj");
			if (zxsjs != null) {
				for (int j = 0; j < zxsjs.length(); j++) {
					JSONObject zxsj = zxsjs.optJSONObject(j);
					Document zxsjDoc = new Document();
					zxsjDoc.put("stime", zxsj.optInt("stime"));
					zxsjDoc.put("etime", zxsj.optInt("etime"));
					zxsjDocs.add(zxsjDoc);
				}
				temp.put("zxsj", zxsjDocs);
			}

			JSONArray crosses = area.optJSONArray("cross");
			if (crosses != null) {
				List<Document> crossDocs = new ArrayList<Document>();
				for (int j = 0; j < crosses.length(); j++) {
					JSONObject cross = crosses.optJSONObject(j);
					Document crossDoc = new Document();
					crossDoc.put("road", cross.optString("road"));
					crossDoc.put("roadName", cross.optString("roadName"));
					crossDoc.put("stName", cross.optString("stName"));
					crossDoc.put("st", cross.optString("st"));
					crossDoc.put("end", cross.optString("end"));
					crossDoc.put("endName", cross.optString("endName"));
					crossDocs.add(crossDoc);
				}
				temp.put("cross", crossDocs);
			}
			JSONObject forbid = area.optJSONObject("forbid");
			if(forbid != null){
				Document fdoc = new Document();
				fdoc.put("stId", forbid.optInt("stId"));
				fdoc.put("subId", forbid.optInt("_id"));
				fdoc.put("subName", forbid.optString("name"));
				fdoc.put("roadName", forbid.optString("dlmc"));
				temp.put("forbid", fdoc);
			}
			temp.put("gxsj", new Date());
			JSONObject stForbid = area.optJSONObject("stForbidObj");
			hpzlList = DaoUtils.megerListMap(hpzlList, stForbid
					.optJSONArray("hpzl"));
			cllxList = DaoUtils.megerListMap(cllxList, stForbid
					.optJSONArray("cllx"));
			temp.put("stForbidName", stForbid.optString("stForbidName"));
			docs.add(temp);
		}
		doc.put("areas", docs);
		doc.put("hpzl", hpzlList);
		doc.put("cllx", cllxList);
		col.insertOne(doc);
		row = doc != null ? 1 : 0;
		return row;
	}

	public static int delpassMb(String id) {
		MongoCollection<Document> col = getCol(passMb);
		DeleteResult dr = col.deleteOne(Filters.eq("_id", Integer.valueOf(id)));
		return (int) dr.getDeletedCount();
	}

	public static boolean isSameNameMb(String mbName) {
		MongoCollection<Document> col = getCol(passMb);
		return col.find(Filters.eq("mbName", mbName)).iterator().hasNext();
	}

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date st = sdf.parse("2017-01-16");
		Date et = sdf.parse("2017-12-16");
		boolean b = ForbidMongoDao.checkPassInfo("01", "鑻廎33106", st, et, 0);
		System.out.println(b);
	}

	public static int updateSpbj(String passId, int ckmod, String spnr,
			String jybh, String mjxm) {
		Bson cond = Filters.eq("_id", Long.valueOf(passId));
		MongoCollection<Document> col = getCol(passInfo);
		Document doc = col.find(cond).first();
		doc.put("spbj", ckmod);
		if (ckmod < 3) {
			doc.put("yxbj", 1);
		}
		doc.put("spnr", spnr);
		doc.put("spmj", jybh);
		doc.put("mjxm", mjxm);
		doc.put("spsj", new Date());
		Document d = col.findOneAndReplace(cond, doc);
		return d == null ? 0 : 1;
	}

	public static int saveRole(int roleId, JSONObject obj) {
		Bson cond = Filters.eq("roleId", roleId);
		MongoCollection<Document> col = getCol(role);
		Document doc = col.find(cond).first();
		List<Integer> first = getIntListByArray(obj.optJSONArray("first"));
		int check = obj.optInt("check");
		List<Integer> catalog = getIntListByArray(obj.optJSONArray("catalog"));
		Document d = new Document();
		d.put("roleId", roleId);
		d.put("first", first);
		d.put("check", check);
		d.put("catalog", catalog);
		System.out.println(d.toJson());
		if (doc == null) {
			col.insertOne(d);
		} else {
			col.findOneAndReplace(cond, d);
		}
		return 1;
	}

	public static int saveTimeMb(String zxsj, String sjmc) {
		long id = getSeq(timeMb);
		MongoCollection<Document> col = getCol(timeMb);
		Document doc = new Document();
		try {
			List<Document> list = new ArrayList<Document>();
			JSONArray sjs = new JSONArray(zxsj);
			for (int i = 0; i < sjs.length(); i++) {
				JSONObject sj = sjs.optJSONObject(i);
				Document d = new Document();
				d.put("stime", sj.optDouble("stime"));
				d.put("etime", sj.optDouble("etime"));
				list.add(d);
			}
			doc.put("zxsj", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		doc.put("_id", (int) id);
		doc.put("sjmc", sjmc);
		col.insertOne(doc);
		return doc == null ? 0 : 1;
	}

	public static JSONArray queryTimeMb() {
		MongoCollection<Document> col = getCol(timeMb);
		ArrayList<Document> docs = col.find().into(new ArrayList<Document>());
		return DaoUtils.docsToJsonArray(docs);
	}

	public static int delTimeMb(String id) {
		MongoCollection<Document> col = getCol(timeMb);
		Bson cond = Filters.eq("_id", Integer.valueOf(id));
		col.deleteOne(cond);
		return (int) col.deleteOne(cond).getDeletedCount();
	}
	
	public static Document queryDocumentById(String id,String tb){
		MongoCollection<Document> col = getCol(tb);
		List<Bson> conds = new ArrayList<Bson>();
		conds.add(Filters.eq("_id", Long.valueOf(id)));
		conds.add(Filters.eq("_id", Integer.valueOf(id)));
		return col.find(Filters.or(conds)).first();
	}

}
