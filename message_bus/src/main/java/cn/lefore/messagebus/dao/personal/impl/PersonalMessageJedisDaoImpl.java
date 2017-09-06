package cn.lefore.messagebus.dao.personal.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.dao.personal.IPersonalMessageJedisDao;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class PersonalMessageJedisDaoImpl implements IPersonalMessageJedisDao {

    private JedisPool jedisPool;

    /**
     * redie zadd
     * 1) uid 收藏了某个sid
     * 2) sid 被那几个uid收藏了
     *
     * @param collMes
     */
    public void addCollMess(String collMes) {
        Jedis jedis = jedisPool.getResource();
        try {
            JSONObject jsonObj = new JSONObject(collMes);

            String uidKeyPrex = ConstantMess.FROM_TV.equals(jsonObj.getString("userType")) ?
                    ConstantMess.TV_COLLECT_MESS_INDEX_KY_PREX : ConstantMess.USER_COLLECT_MESS_INDEX_KY_PREX; //t:collect:  &&  u:collect:
            String uidCollKey = uidKeyPrex + jsonObj.getString("uid"); //t:collect:  ||  u:collect: + uid

            String sidCollKey = ConstantMess.SID_COLLECT_MESS_INDEX_KY_PREX + jsonObj.getString("sid"); //sid:uid:collect: + sid
            System.out.println("# PersonalMessageJedisDaoImpl.addCollMess(), uidCollKey=" + uidCollKey + "; sidCollKey=" + sidCollKey);

            String scoreTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            long scoreTime = Long.parseLong(scoreTimeStr);

            jedis.zadd(uidCollKey, scoreTime, jsonObj.getString("sid"));

            /*
                一对多(sid对uid)，后加的2016-01-30
                ZRANGE sid:uid:collect:3fx0ab9xd4a1 0 -1
                1) "31859208"
                2) "45221858"   //add this
             */
            jedis.zadd(sidCollKey, scoreTime, jsonObj.getString("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public void delCollMess(String collMes) {
        Jedis jedis = jedisPool.getResource();
        try {
            JSONObject jsonObj = new JSONObject(collMes);
            String keyPrex = ConstantMess.FROM_TV.equals(jsonObj.getString("userType")) ? ConstantMess.TV_COLLECT_MESS_INDEX_KY_PREX : ConstantMess.USER_COLLECT_MESS_INDEX_KY_PREX;
            String uidCollKey = keyPrex + jsonObj.getString("uid"); //t:collect:  ||  u:collect: + uid

            String sidCollKey = ConstantMess.SID_COLLECT_MESS_INDEX_KY_PREX + jsonObj.getString("sid");
            System.out.println("# PersonalMessageJedisDaoImpl.addCollMess(), collKey=" + uidCollKey + "; sidCollKey=" + sidCollKey);

            jedis.zrem(uidCollKey, jsonObj.getString("sid"));
            jedis.zrem(sidCollKey, jsonObj.getString("uid"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public void bufferAndProcessMess(String jsonMess) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.lpush(ConstantMess.BUFFER_KEY, jsonMess);
        } finally {
            jedis.close();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
