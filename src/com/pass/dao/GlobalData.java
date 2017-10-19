package com.pass.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalData {
	
	public static Map<String, String> dicMap = new HashMap<String, String>();
	public static Map<String, String> cllxMap = new HashMap<String, String>();
	public static Map<String, String> hpzlMap = new HashMap<String, String>();
	public static Map<String, String> passAgeMap = new LinkedHashMap<String, String>();
	public static Map<String, String> checkReqMap = new LinkedHashMap<String, String>();
	public static Map<String, String> repeatMap = new LinkedHashMap<String, String>();
	public static Map<String, String> timeUnitMap = new LinkedHashMap<String, String>();
	public static Map<String, String> isFirstMap = new LinkedHashMap<String, String>();
	public static Map<String, Map<String, String>> passMap = new HashMap<String, Map<String, String>>();
	public static Map<String, String> subCatalogMap = new LinkedHashMap<String, String>();
	public static Map<String, String> yxbjMap = new LinkedHashMap<String, String>();
	public static Map<String, String> spbjMap = new LinkedHashMap<String, String>();
	public static Map<String, String> isAtmMap = new LinkedHashMap<String, String>();
	static {
		passAgeMap.put("0", "临时");
		passAgeMap.put("1", "长期");
		checkReqMap.put("0", "无需审核");
		checkReqMap.put("1", "一级审核");
		checkReqMap.put("2", "二级审核");
		repeatMap.put("0", "不可延期");
		repeatMap.put("1", "可以延期");
		timeUnitMap.put("1", "天");
		timeUnitMap.put("2", "月");
		isFirstMap.put("1", "首次办理");
		isFirstMap.put("0", "办证延期");
		isAtmMap.put("0", "不可自助");
		isAtmMap.put("1", "可以自助");
		subCatalogMap.put("0", "禁行区域");
		subCatalogMap.put("1", "准行区域");
		yxbjMap.put("0", "未生效");
		yxbjMap.put("1", "已生效");
		spbjMap.put("0", "未审批");
		spbjMap.put("1", "一级审批通过");
		spbjMap.put("2", "二级审批通过");
		spbjMap.put("3", "一级审批否定");
		spbjMap.put("4", "二级审批否定");
		passMap.put("passAge", passAgeMap);
		passMap.put("check", checkReqMap);
		passMap.put("repeat", repeatMap);
		passMap.put("timeUnit", timeUnitMap);
		passMap.put("isFirst", isFirstMap);
		passMap.put("subCatalog", subCatalogMap);
		passMap.put("yxbj", yxbjMap);
		passMap.put("spbj", spbjMap);
		passMap.put("isAtm", isAtmMap);

		dicMap.put("hpzl", "00,1007");
		dicMap.put("cllx", "00,1004");
		dicMap.put("syxz", "00,1003");
		dicMap.put("ryfl", "04,0080");
		dicMap.put("clfl", "04,0081");
		dicMap.put("zzmm", "00,6131");
		dicMap.put("zyxx", "04,0101");
		dicMap.put("clzt", "00,1017");
		dicMap.put("csys", "00,1008");
		dicMap.put("cllx", "00,1004");

		cllxMap.put("B1", "重型半挂车");
		cllxMap.put("B2", "中型半挂车");
		cllxMap.put("B3", "轻型半挂车");
		cllxMap.put("D1", "电车");
		cllxMap.put("G1", "重型全挂车");
		cllxMap.put("G2", "中型全挂车");
		cllxMap.put("G3", "轻型全挂车");
		cllxMap.put("H1", "重型货车");
		cllxMap.put("H2", "中型货车");
		cllxMap.put("H3", "轻型货车");
		cllxMap.put("H4", "微型货车");
		cllxMap.put("H5", "低速货车");
		cllxMap.put("J1", "轮式机械");
		cllxMap.put("K1", "大型客车");
		cllxMap.put("K2", "中型客车");
		cllxMap.put("K3", "小型客车");
		cllxMap.put("K4", "微型客车");
		cllxMap.put("M1", "三轮摩托车");
		cllxMap.put("M2", "二轮摩托车");
		cllxMap.put("N1", "三轮汽车");
		cllxMap.put("Q1", "重型牵引车");
		cllxMap.put("Q2", "中型牵引车");
		cllxMap.put("Q3", "轻型牵引车");
		cllxMap.put("T1", "大型拖拉机");
		cllxMap.put("T2", "小型拖拉机");
		cllxMap.put("Z1", "大型作业车");
		cllxMap.put("Z2", "中型作业车");
		cllxMap.put("Z3", "小型作业车");
		cllxMap.put("Z4", "微型作业车");
		cllxMap.put("Z5", "重型作业车");
		cllxMap.put("Z7", "轻型作业车");
		ForbidOraDao dao = new ForbidOraDao(false);
		dao.close();
	}

}
