package com.pass.dao;

import com.lixd.jdbc.util.TextUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class WebRedisDao {
	private final String SJHM_YZM_FSSJ = "sjhm_yzm_fssj:";
	private final String BD_SJHM_YZM = "bd_sjhm_yzm:";
	private final String SJHM_COUNT_DAY = "sjhm_count:";

	private Jedis jedis = null;

	public WebRedisDao() {
		JedisShardInfo js = new JedisShardInfo("58.221.183.194", 6379, 10000);
		js.setPassword("lixd17820");
		try {
			jedis = new Jedis(js);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnOk() {
		return jedis != null;
	}

	public int getSjhCmount(String sjhm) {
		String key = SJHM_COUNT_DAY + sjhm;
		String v = jedis.get(key);
		if (v == null)
			return 0;
		return Integer.valueOf(v);
	}

	/**
	 * �Ƿ���С��2���ӵ�ʱ���������ط�
	 * 
	 * @param sjhm
	 * @return
	 */
	public boolean isSjyzmFssjShort(String sjhm) {
		String key = SJHM_YZM_FSSJ + sjhm;
		String scu = jedis.get(key);
		if (TextUtils.isEmpty(scu))
			return false;
		long cu = Long.valueOf(scu);
		long sjjg = System.currentTimeMillis() - cu;
		return sjjg < 2 * 60 * 1000;
	}

	public String getBdSjhmYzm(String sjhm) {
		String key = BD_SJHM_YZM + sjhm;
		String v = jedis.get(key);
		return v;
	}

	/**
	 * �����ֻ���֤��
	 * 
	 * @param sjhm
	 * @param yzm
	 */
	public void saveBdSjhmYzm(String sjhm, String yzm) {
		String key = BD_SJHM_YZM + sjhm;
		jedis.set(key, yzm);
		jedis.expire(key, 600);
		setSjyzmFssj(sjhm);
		key = SJHM_COUNT_DAY + sjhm;
		jedis.incr(key);
		jedis.expire(key, 60 * 60 * 24);
	}

	/**
	 * �趨�ֻ���֤��ķ���ʱ��
	 * 
	 * @param sjhm
	 */
	public void setSjyzmFssj(String sjhm) {
		String key2 = SJHM_YZM_FSSJ + sjhm;
		jedis.set(key2, System.currentTimeMillis() + "");
		jedis.expire(key2, 600);
	}

	public void closeConn() {
		jedis.disconnect();
	}

	/**
	 * ��֤���Ƿ���ȷ
	 * 
	 * @param sjhm
	 * @param yzm
	 * @return
	 */
	public boolean checkBdSjhmYzm(String sjhm, String yzm) {
		if (TextUtils.isEmpty(yzm))
			return false;
		String v = getBdSjhmYzm(sjhm);
		return TextUtils.equals(v, yzm);
	}

	/**
	 * ��֤��Ϻ�ɾ����֤��
	 * 
	 * @param sjhm
	 */
	public void delBdSjhmYzm(String sjhm) {
		String key = BD_SJHM_YZM + sjhm;
		jedis.del(key);
		String key2 = SJHM_YZM_FSSJ + sjhm;
		jedis.del(key2);
	}

}
