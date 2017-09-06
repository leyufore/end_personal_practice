package cn.lefore.messagebus.service.personal.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.Coder;
import cn.lefore.messagebus.dao.personal.IPersonalMessageJedisDao;
import cn.lefore.messagebus.dao.personal.impl.PersonalMessageJedisDaoImpl;
import cn.lefore.messagebus.service.personal.IPersonalMessageService;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class PersonalMessageServiceImpl implements IPersonalMessageService {

    private IPersonalMessageJedisDao personalMessageJedisDao;

    public void syncMessagePool(Object collMess) {
        try {
            JSONObject jsonObj = new JSONObject(collMess);
            if (ConstantMess.OP_ADD.equalsIgnoreCase(jsonObj.getString("operation"))) {
                personalMessageJedisDao.addCollMess(collMess.toString());
            }
            if (ConstantMess.OP_DEL.equalsIgnoreCase(jsonObj.getString("operation"))) {
                personalMessageJedisDao.delCollMess(collMess.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 此时的实现我认为并不好，不应该从 dao 里面再去获取 Pool 出来使用。
     * 如果是避免因 lpush、lrem ，又新开两个方法
     * 应该做一个父类 BaseJedisDao，里面包含所有简单的基本操作
     * 其余与业务相关的 dao 继承
     *
     * @param starMess
     */
    public void star2Uid(Object starMess) {
        JedisPool pool = ((PersonalMessageJedisDaoImpl) personalMessageJedisDao).getJedisPool();
        Jedis jedis = pool.getResource();
        try {
            JSONObject jsonObject = new JSONObject(starMess.toString());
            String star = jsonObject.get("star").toString();
            String base64Name = Coder.getBASE64(star);
            String star2UidKey = ConstantMess.STAR_PREX + base64Name;

            if (ConstantMess.OP_ADD.equals(jsonObject.getString("op"))) {
                jedis.lpush(star2UidKey, jsonObject.getString("uid"));
            }
            if (ConstantMess.OP_DEL.equals(jsonObject.getString("op"))) {
                jedis.lrem(star2UidKey, 0, jsonObject.getString("uid"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public IPersonalMessageJedisDao getPersonalMessageJedisDao() {
        return personalMessageJedisDao;
    }

    public void setPersonalMessageJedisDao(IPersonalMessageJedisDao personalMessageJedisDao) {
        this.personalMessageJedisDao = personalMessageJedisDao;
    }
}
