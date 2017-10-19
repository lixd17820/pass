package com.pass.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.lixd.jdbc.util.TextUtils;
import com.mongodb.client.model.Filters;
import com.pass.dao.ForbidMongoDao;
import com.pass.dao.ForbidOraDao;
import com.pass.dao.GlobalMethod;
import com.pass.dao.RestfulClient;
import com.pass.dao.WebRedisDao;

@Path("pass")
public class PassService {
	@Context
	private HttpServletRequest request;

	@GET
	@Path("test")
	@Produces("application/json")
	public String test(@QueryParam("param") String param) {
		return param;
	}

	private String createRandomStr() {
		Random random = new Random();
		String s = String.valueOf(Math.abs(random.nextInt()) % 1000000);
		s = padLeftZero(s, 6);
		return s;
	}

	private String padLeftZero(String s, int len) {
		for (int i = s.length(); i < len; i++) {
			s = "0" + s;
		}
		return s;
	}

	private String getJsonStr(String key, String val) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(key, val);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	@GET
	@Path("sendSjyzm")
	@Produces("application/json")
	public String sendSjyzm(@QueryParam("sjhm") String sjhm,
			@QueryParam("rCode") String rCode) {
		String rc = (String) request.getSession().getAttribute("rCode");
		System.out.println(rc + "," + rCode);
		if (!TextUtils.equals(rc, rCode))
			return getJsonStr("err", "��ҳ�ѹ��ڣ���ˢ��");
		WebRedisDao rdao = new WebRedisDao();
		try {
			if (!rdao.isConnOk()) {
				return getJsonStr("err", "�������");
			}
			int c = rdao.getSjhCmount(sjhm);
			if (c > 5) {
				return getJsonStr("err", "һ��һ�ֻ�ֻ�ܷ���5��");
			}
			boolean isShort = rdao.isSjyzmFssjShort(sjhm);
			if (isShort) {
				return getJsonStr("err", "����ʱ��������");
			}
			String text = "";
			text = createRandomStr();
			String[] detail = GlobalMethod.sendSmsDetail("������֤��Ϊ��" + text
					+ "�������Ʊ��棬10��������Ч��", sjhm);
			if (!"0".equals(detail[0])) {
				return getJsonStr("err", detail[1]);
			}
			rdao.saveBdSjhmYzm(sjhm, text);
		} catch (Exception e) {
			e.printStackTrace();
			return getJsonStr("err", "�������");
		}
		return getJsonStr("ok", "��֤�뷢�ͳɹ�");
	}

	private static String[] whites = new String[] { "��FW051Y", "��F6HL53",
			"��F172ZL", "��F1HS29", "��FAZ291", "��F8751E", "��FP266Z", "��FLD251",
			"��F9NL81", "��F8K513", "��FD9020", "��F4R251", "��F9N162", "��F179P7",
			"��FD4719", "��FFM089", "��FM697B", "��FE850G", "��F8CE23", "��FX262E",
			"��FER366", "��FX350L", "��FX263B", "��FQ212B", "��FPK509", "��F96563",
			"��F8G593", "��FXE980", "��F96715", "��F8N893", "��F195KM", "��FPE211",
			"��F5HF91", "��F1U788", "��FF019G", "��F889P7", "��FD5016", "��F29637",
			"��FE276G", "��FFZ219", "��F9HP11", "��F1U788", "��FL109F", "��F221P7",
			"��F93700", "��F67753", "��F2375L", "��F2N132", "��F7EQ61", "��F3R379" };
	private static List<String> whiteList = Arrays.asList(whites);

	@POST
	@Path("atmPass")
	@Produces("application/json")
	public String atmPass(@FormParam("hpzl") String hpzl,
			@FormParam("hphm") String hphm, @FormParam("sbdm") String sbdm,
			@FormParam("sjhm") String sjhm, @FormParam("jhm") String jhm,
			@FormParam("sxrq") String sxrq, @FormParam("glbm") String glbm,
			@FormParam("rCode") String rCode) {
		String rc = (String) request.getSession().getAttribute("rCode");
		if (!TextUtils.equals(rc, rCode))
			return getJsonStr("err", "��ҳ�ѹ��ڣ���ˢ��");
		WebRedisDao rdao = new WebRedisDao();
		if (!rdao.checkBdSjhmYzm(sjhm, jhm))
			return getJsonStr("err", "�ֻ����������");
		hphm = hphm.toUpperCase().trim();
		ForbidOraDao dao = new ForbidOraDao(false);
		JSONObject obj = dao.queryVeh(hpzl, hphm);
		if (obj == null && !hphm.startsWith("��F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
		}
		System.out.println(obj);
		dao.close();
		if (obj == null) {
			return getJsonStr("err", "��ѯ�����ó���Ϣ");
		}
		String cllx = obj.optString("cllx");
		if (TextUtils.isEmpty(cllx) || cllx.length() != 3) {
			return getJsonStr("err", "��ѯ�����ó��ĳ������ͣ��޷�����ͨ��֤");
		}
		String sCllx = cllx.substring(0, 2);
		// ��֤�Ƿ���԰���ͨ��֤���������͡�ʹ�����ʡ�����״̬��������
		List<String> tempCllx = Arrays.asList(new String[] { "H21", "H22",
				"H23", "H29", "H3", "H4", "Z31", "Z32", "Z41", "Z42", "Z71",
				"Z72" });
		if (!tempCllx.contains(sCllx) && !tempCllx.contains(cllx)) {
			return getJsonStr("err", "���ĳ������Ͳ�������������ͨ��֤");
		}
		String syr = obj.optString("syr");
		if (hphm.startsWith("��F")) {
			// ��֤���س���״̬
			if (!whiteList.contains(hphm)) {
				String zt = obj.optString("zt");
				if (TextUtils.isEmpty(zt) || zt.indexOf("A") < 0) {
					return getJsonStr("err", "���ػ�����״̬����Ϊ����������ȡͨ��֤");
				}
			}
		}
		String clsbdh = obj.optString("clsbdh");
		if (TextUtils.isEmpty(clsbdh)
				|| clsbdh.length() < 4
				|| !TextUtils.equals(clsbdh.substring(clsbdh.length() - 4),
						sbdm)) {
			return getJsonStr("err", "����ʶ��������");
		}
		rdao.delBdSjhmYzm(sjhm);
		// String areas =
		// "[{\"stForbid\":\"12\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}],\"forbid\":{\"_id\":15,\"name\":\"�����������Ľ���\",\"dlmc\":\"����·�������ﱱ·����������·��Ҧ��·������·����ũ·Χ�ɵ����򣬲�������·\",}},{\"stForbid\":\"16\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]},{\"stForbid\":\"14\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]}]";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (TextUtils.equals(sxrq, "2")) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		String stDate = sdf.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, 1);
		String endDate = sdf.format(c.getTime());
		JSONObject re = RestfulClient.savePassInfo(hpzl, hphm, cllx, stDate,
				endDate, sjhm, glbm, syr);
		if (re == null)
			return getJsonStr("err", "���ش���");
		return re.toString();
	}

	@POST
	@Path("queryOutVeh")
	@Produces("application/json")
	public String queryOutVeh(@FormParam("hpzl") String hpzl,
			@FormParam("hphm") String hphm) {
		if (TextUtils.isEmpty(hpzl) && TextUtils.isEmpty(hphm)) {
			return getJsonStr("err", "����ȫΪ��");
		}
		ForbidOraDao dao = new ForbidOraDao(true);
		JSONObject array = dao.queryOutVeh(hpzl, hphm);
		dao.close();
		return array.toString();
	}

	@POST
	@Path("queryVeh")
	@Produces("application/json")
	public String queryVeh(@FormParam("hpzl") String hpzl,
			@FormParam("hphm") String hphm) {
		if (TextUtils.isEmpty(hpzl) || TextUtils.isEmpty(hphm)) {
			return getJsonStr("err", "����Ϊ��");
		}
		hphm = hphm.toUpperCase().trim();
		ForbidOraDao dao = new ForbidOraDao(true);
		JSONObject obj = dao.queryVeh(hpzl, hphm);
		System.out.println("���ز�ѯ�����" + obj);
		if (obj == null && !hphm.startsWith("��F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
			System.out.println("�ӿڲ�ѯ�����" + obj);
			if (obj != null && TextUtils.isEmpty(obj.optString("err"))) {
				boolean is = dao.isAddOutVeh(obj.optString("hpzl"),
						obj.optString("hphm"));
				if (!is) {
					dao.addOutVehicle(obj);
				}
			}
		}
		dao.close();
		if (obj == null || !TextUtils.isEmpty(obj.optString("err"))) {
			return getJsonStr("err", "û�в�ѯ��������");
		}
		try {
			obj.put("hphm", hphm);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	@POST
	@Path("queryPassInfo")
	@Produces("application/json")
	public String queryPassInfo(@FormParam("id") String id,
			@FormParam("hpzl") String hpzl, @FormParam("hphm") String hphm,
			@FormParam("sbdm") String sbdm) {
		hphm = hphm.toUpperCase().trim();
		ForbidOraDao dao = new ForbidOraDao(false);
		JSONObject obj = dao.queryVeh(hpzl, hphm);
		if (obj == null && !hphm.startsWith("��F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
		}
		System.out.println(obj);
		dao.close();
		if (obj == null) {
			return getJsonStr("err", "��ѯ�����ó���Ϣ");
		}
		String clsbdh = obj.optString("clsbdh");
		if (TextUtils.isEmpty(clsbdh)
				|| clsbdh.length() < 4
				|| !TextUtils.equals(clsbdh.substring(clsbdh.length() - 4),
						sbdm)) {
			return getJsonStr("err", "����ʶ��������");
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Bson> conds = new ArrayList<Bson>();
		if (!TextUtils.isEmpty(id)) {
			conds.add(Filters.eq("_id", Long.valueOf(id)));
		} else {
			if (!TextUtils.isEmpty(hpzl)) {
				conds.add(Filters.eq("hpzl", hpzl));
			}
			if (!TextUtils.isEmpty(hphm)) {
				conds.add(Filters.eq("hphm", hphm));
			}
		}
		JSONArray areaArray = ForbidMongoDao.queryArea(conds,
				ForbidMongoDao.passInfo, "_id");
		if (areaArray == null || areaArray.length() == 0)
			return getJsonStr("err", "û�в�ѯ�����������ļ�¼");
		return areaArray.toString();
	}

	public static void main(String[] args) {
		String s = "[{\"stForbid\":\"12\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}],\"forbid\":{\"_id\":15,\"name\":\"�����������Ľ���\",\"dlmc\":\"����·�������ﱱ·����������·��Ҧ��·������·����ũ·Χ�ɵ����򣬲�������·\",}},{\"stForbid\":\"16\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]},{\"stForbid\":\"14\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]}]";
		try {
			JSONArray array = new JSONArray(s);
			System.out.println(array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}