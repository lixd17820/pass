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
		passAgeMap.put("0", "��ʱ");
		passAgeMap.put("1", "����");
		checkReqMap.put("0", "�������");
		checkReqMap.put("1", "һ�����");
		checkReqMap.put("2", "�������");
		repeatMap.put("0", "��������");
		repeatMap.put("1", "��������");
		timeUnitMap.put("1", "��");
		timeUnitMap.put("2", "��");
		isFirstMap.put("1", "�״ΰ���");
		isFirstMap.put("0", "��֤����");
		isAtmMap.put("0", "��������");
		isAtmMap.put("1", "��������");
		subCatalogMap.put("0", "��������");
		subCatalogMap.put("1", "׼������");
		yxbjMap.put("0", "δ��Ч");
		yxbjMap.put("1", "����Ч");
		spbjMap.put("0", "δ����");
		spbjMap.put("1", "һ������ͨ��");
		spbjMap.put("2", "��������ͨ��");
		spbjMap.put("3", "һ��������");
		spbjMap.put("4", "����������");
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

		cllxMap.put("B1", "���Ͱ�ҳ�");
		cllxMap.put("B2", "���Ͱ�ҳ�");
		cllxMap.put("B3", "���Ͱ�ҳ�");
		cllxMap.put("D1", "�糵");
		cllxMap.put("G1", "����ȫ�ҳ�");
		cllxMap.put("G2", "����ȫ�ҳ�");
		cllxMap.put("G3", "����ȫ�ҳ�");
		cllxMap.put("H1", "���ͻ���");
		cllxMap.put("H2", "���ͻ���");
		cllxMap.put("H3", "���ͻ���");
		cllxMap.put("H4", "΢�ͻ���");
		cllxMap.put("H5", "���ٻ���");
		cllxMap.put("J1", "��ʽ��е");
		cllxMap.put("K1", "���Ϳͳ�");
		cllxMap.put("K2", "���Ϳͳ�");
		cllxMap.put("K3", "С�Ϳͳ�");
		cllxMap.put("K4", "΢�Ϳͳ�");
		cllxMap.put("M1", "����Ħ�г�");
		cllxMap.put("M2", "����Ħ�г�");
		cllxMap.put("N1", "��������");
		cllxMap.put("Q1", "����ǣ����");
		cllxMap.put("Q2", "����ǣ����");
		cllxMap.put("Q3", "����ǣ����");
		cllxMap.put("T1", "����������");
		cllxMap.put("T2", "С��������");
		cllxMap.put("Z1", "������ҵ��");
		cllxMap.put("Z2", "������ҵ��");
		cllxMap.put("Z3", "С����ҵ��");
		cllxMap.put("Z4", "΢����ҵ��");
		cllxMap.put("Z5", "������ҵ��");
		cllxMap.put("Z7", "������ҵ��");
		ForbidOraDao dao = new ForbidOraDao(false);
		dao.close();
	}

}
