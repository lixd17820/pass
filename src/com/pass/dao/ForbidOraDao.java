package com.pass.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.lixd.jdbc.util.JdbcUtils;
import com.lixd.jdbc.util.TextUtils;

public class ForbidOraDao {

	private Connection conn;

	public ForbidOraDao(boolean isDatasource) {
		try {
			if (isDatasource) {
				Context initCtx = new InitialContext();
				Context envContext = (Context) initCtx.lookup("java:comp/env");
				DataSource mySource = (DataSource) envContext
						.lookup("jdbc/myoracle");
				conn = mySource.getConnection();
			} else {
				DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
				this.conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@10.36.6.20:1521:orcl2", "zh2004",
						"zh2008");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JSONObject queryOutVeh(String hpzl, String hphm) {
		String sql = "select * from T_JWT_OUT_VEHICLE t where hpzl='" + hpzl
				+ "' and hphm ='" + hphm + "'";
		JSONObject obj = JdbcUtils.sqlGetOneJsonRow(sql, conn);
		return obj;
	}

	public JSONObject queryVeh(String hpzl, String hphm) {
		hphm = hphm.toUpperCase().trim();
		String sqlOut = "select XH,HPZL,HPHM,CLPP1,CLXH,CLSBDH,FDJH,CLLX,CSYS,SYXZ,SFZMHM,SYR,CCDJRQ,FZJG,ZT from T_JWT_OUT_VEHICLE "
				+ " where hpzl='" + hpzl + "' and hphm ='" + hphm + "'";
		String sqlIn = "select XH,HPZL,HPHM,CLPP1,CLXH,CLSBDH,FDJH,CLLX,CSYS,SYXZ,SFZMHM,SYR,CCDJRQ,FZJG,ZT,YXQZ,zsxxdz from trff_app.vehicle "
				+ " where hpzl='"
				+ hpzl
				+ "' and hphm ='"
				+ hphm.substring(1)
				+ "'";
		String sql = hphm.startsWith("苏F") ? sqlIn : sqlOut;
		return JdbcUtils.sqlGetOneJsonRow(sql, conn);
	}

	/**
	 * 将外地车加到临时存储库中
	 * 
	 * @param veh
	 * @return
	 */
	public int addOutVehicle(JSONObject veh) {
		try {
			veh.put("hphm", veh.optString("hphm").toUpperCase().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		boolean isAdd = JdbcUtils.sqlIsTableHasRow("T_JWT_OUT_VEHICLE",
				new String[] { "hpzl", "hphm" },
				new String[] { veh.optString("hpzl"), veh.optString("hphm") },
				conn);
		if (isAdd)
			return -1;
		String sql = "insert into T_JWT_OUT_VEHICLE "
				+ "(id,xh,hpzl,hphm,clpp1,clxh,clsbdh,fdjh,cllx,csys,syxz,syr,ccdjrq,zt,fzjg) "
				+ "values(seq_jwt_out_vehicle.nextval,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
		return JdbcUtils.sqlTempleteExecuteUpdate(
				sql,
				new String[] { veh.optString("xh"), veh.optString("hpzl"),
						veh.optString("hphm"), veh.optString("clpp1"),
						veh.optString("clxh"), veh.optString("clsbdh"),
						veh.optString("fdjh"), veh.optString("cllx"),
						veh.optString("csys"), veh.optString("syxz"),
						veh.optString("syr"), veh.optString("ccdjrq"),
						veh.optString("zt"),
						veh.optString("hphm").substring(0, 2) }, conn);
	}

	public boolean isAddOutVeh(String hpzl, String hphm) {
		boolean isAdd = JdbcUtils.sqlIsTableHasRow("T_JWT_OUT_VEHICLE",
				new String[] { "hpzl", "hphm" }, new String[] { hpzl, hphm },
				conn);
		return isAdd;
	}

}
