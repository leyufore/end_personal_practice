package cn.lefore.messagebus.dao.program.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.LogHelper;
import cn.lefore.messagebus.dao.program.IProgramMessageJedisDao;
import cn.lefore.messagebus.manager.ILongConnectManage;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class ProgramMessageJedisDaoImpl implements IProgramMessageJedisDao {

    private static final Logger log = LogHelper.getLogger();
    private JedisPool jedisPool;
    private ILongConnectManage connectManage;

    /**
     * 节目更新推送消息相应收藏了节目的用户
     *
     * @param updateMess
     */
    public void updateProgram(String updateMess) {
        Jedis jedis = jedisPool.getResource();
        try {
            JSONObject jsonObject = new JSONObject(updateMess);
            String sidUpdated = jsonObject.getString("sid");
            String programKey = ConstantMess.UPDATED_PROGRAM_MESS_KY_PREX + sidUpdated;
            jedis.set(programKey, updateMess);
            //暂时把更新的sid存到redis中，先不进行推送【针对 metis 极光推送】
            jedis.rpush("new", sidUpdated);

            Set<String> uidSet = jedis.zrange(ConstantMess.SID_COLLECT_MESS_INDEX_KY_PREX + sidUpdated, 0, -1);
            log.info("uidset = " + uidSet + " 若为空不触发长连接");
            List<String> uidList = new ArrayList<String>();
            for (String uid : uidSet) {
                uidList.add(uid);
            }
            JSONObject connectJson = connectManage.getConnectJson();
            connectManage.sendMessageToIca(connectJson.toString(), uidList, ConstantMess.FORUSERORCLIENT);
        } catch (JSONException e) {
            e.printStackTrace();
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

    public ILongConnectManage getConnectManage() {
        return connectManage;
    }

    public void setConnectManage(ILongConnectManage connectManage) {
        this.connectManage = connectManage;
    }
}
