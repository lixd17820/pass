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
			return getJsonStr("err", "网页已过期，新刷新");
		WebRedisDao rdao = new WebRedisDao();
		try {
			if (!rdao.isConnOk()) {
				return getJsonStr("err", "网络错误");
			}
			int c = rdao.getSjhCmount(sjhm);
			if (c > 5) {
				return getJsonStr("err", "一天一手机只能发送5次");
			}
			boolean isShort = rdao.isSjyzmFssjShort(sjhm);
			if (isShort) {
				return getJsonStr("err", "发送时间间隔过短");
			}
			String text = "";
			text = createRandomStr();
			String[] detail = GlobalMethod.sendSmsDetail("短信验证码为：" + text
					+ "，请妥善保存，10分钟内有效。", sjhm);
			if (!"0".equals(detail[0])) {
				return getJsonStr("err", detail[1]);
			}
			rdao.saveBdSjhmYzm(sjhm, text);
		} catch (Exception e) {
			e.printStackTrace();
			return getJsonStr("err", "网络错误");
		}
		return getJsonStr("ok", "验证码发送成功");
	}

	private static String[] whites = new String[] { "苏FW051Y", "苏F6HL53",
			"苏F172ZL", "苏F1HS29", "苏FAZ291", "苏F8751E", "苏FP266Z", "苏FLD251",
			"苏F9NL81", "苏F8K513", "苏FD9020", "苏F4R251", "苏F9N162", "苏F179P7",
			"苏FD4719", "苏FFM089", "苏FM697B", "苏FE850G", "苏F8CE23", "苏FX262E",
			"苏FER366", "苏FX350L", "苏FX263B", "苏FQ212B", "苏FPK509", "苏F96563",
			"苏F8G593", "苏FXE980", "苏F96715", "苏F8N893", "苏F195KM", "苏FPE211",
			"苏F5HF91", "苏F1U788", "苏FF019G", "苏F889P7", "苏FD5016", "苏F29637",
			"苏FE276G", "苏FFZ219", "苏F9HP11", "苏F1U788", "苏FL109F", "苏F221P7",
			"苏F93700", "苏F67753", "苏F2375L", "苏F2N132", "苏F7EQ61", "苏F3R379" };
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
			return getJsonStr("err", "网页已过期，新刷新");
		WebRedisDao rdao = new WebRedisDao();
		if (!rdao.checkBdSjhmYzm(sjhm, jhm))
			return getJsonStr("err", "手机激活码错误");
		hphm = hphm.toUpperCase().trim();
		ForbidOraDao dao = new ForbidOraDao(false);
		JSONObject obj = dao.queryVeh(hpzl, hphm);
		if (obj == null && !hphm.startsWith("苏F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
		}
		System.out.println(obj);
		dao.close();
		if (obj == null) {
			return getJsonStr("err", "查询不到该车信息");
		}
		String cllx = obj.optString("cllx");
		if (TextUtils.isEmpty(cllx) || cllx.length() != 3) {
			return getJsonStr("err", "查询不到该车的车辆类型，无法办理通行证");
		}
		String sCllx = cllx.substring(0, 2);
		// 验证是否可以办理通行证，车辆类型、使用性质、车辆状态、黑名单
		List<String> tempCllx = Arrays.asList(new String[] { "H21", "H22",
				"H23", "H29", "H3", "H4", "Z31", "Z32", "Z41", "Z42", "Z71",
				"Z72" });
		if (!tempCllx.contains(sCllx) && !tempCllx.contains(cllx)) {
			return getJsonStr("err", "您的车辆类型不可以自助办理通行证");
		}
		String syr = obj.optString("syr");
		if (hphm.startsWith("苏F")) {
			// 验证本地车的状态
			if (!whiteList.contains(hphm)) {
				String zt = obj.optString("zt");
				if (TextUtils.isEmpty(zt) || zt.indexOf("A") < 0) {
					return getJsonStr("err", "本地机动车状态必须为正常才能领取通行证");
				}
			}
		}
		String clsbdh = obj.optString("clsbdh");
		if (TextUtils.isEmpty(clsbdh)
				|| clsbdh.length() < 4
				|| !TextUtils.equals(clsbdh.substring(clsbdh.length() - 4),
						sbdm)) {
			return getJsonStr("err", "车辆识别代码错误");
		}
		rdao.delBdSjhmYzm(sjhm);
		// String areas =
		// "[{\"stForbid\":\"12\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}],\"forbid\":{\"_id\":15,\"name\":\"市区货车核心禁区\",\"dlmc\":\"钟秀路、孩儿巷北路、孩儿巷南路、姚港路、虹桥路、工农路围成的区域，不含钟秀路\",}},{\"stForbid\":\"16\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]},{\"stForbid\":\"14\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]}]";
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
			return getJsonStr("err", "返回错误");
		return re.toString();
	}

	@POST
	@Path("queryOutVeh")
	@Produces("application/json")
	public String queryOutVeh(@FormParam("hpzl") String hpzl,
			@FormParam("hphm") String hphm) {
		if (TextUtils.isEmpty(hpzl) && TextUtils.isEmpty(hphm)) {
			return getJsonStr("err", "不能全为空");
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
			return getJsonStr("err", "不能为空");
		}
		hphm = hphm.toUpperCase().trim();
		ForbidOraDao dao = new ForbidOraDao(true);
		JSONObject obj = dao.queryVeh(hpzl, hphm);
		System.out.println("本地查询结果：" + obj);
		if (obj == null && !hphm.startsWith("苏F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
			System.out.println("接口查询结果：" + obj);
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
			return getJsonStr("err", "没有查询到该数据");
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
		if (obj == null && !hphm.startsWith("苏F")) {
			obj = RestfulClient.queryQgVeh(hpzl, hphm);
		}
		System.out.println(obj);
		dao.close();
		if (obj == null) {
			return getJsonStr("err", "查询不到该车信息");
		}
		String clsbdh = obj.optString("clsbdh");
		if (TextUtils.isEmpty(clsbdh)
				|| clsbdh.length() < 4
				|| !TextUtils.equals(clsbdh.substring(clsbdh.length() - 4),
						sbdm)) {
			return getJsonStr("err", "车辆识别代码错误");
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
			return getJsonStr("err", "没有查询到符合条件的记录");
		return areaArray.toString();
	}

	public static void main(String[] args) {
		String s = "[{\"stForbid\":\"12\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}],\"forbid\":{\"_id\":15,\"name\":\"市区货车核心禁区\",\"dlmc\":\"钟秀路、孩儿巷北路、孩儿巷南路、姚港路、虹桥路、工农路围成的区域，不含钟秀路\",}},{\"stForbid\":\"16\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]},{\"stForbid\":\"14\",\"subCatalog\":\"0\",\"zxsj\":[{\"stime\":9,\"etime\":17}]}]";
		try {
			JSONArray array = new JSONArray(s);
			System.out.println(array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}