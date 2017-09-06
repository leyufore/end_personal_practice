package cn.lefore.messagebus.dao.system.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.LogHelper;
import cn.lefore.messagebus.common.utils.Myjob;
import cn.lefore.messagebus.common.utils.QuartzManager;
import cn.lefore.messagebus.dao.system.ISystemMessageJedisDao;
import cn.lefore.messagebus.manager.ILongConnectManage;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.JobDataMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wenrule on 2017/7/31.
 */
public class SystemMessageJedisDaoImpl implements ISystemMessageJedisDao {

    private JedisPool jedisPool;
    private ILongConnectManage connectManage;

    private static final Logger logger = LogHelper.getLogger();

    public void save(String sysMess) {
        Jedis jedis = jedisPool.getResource();

        try {
            JSONObject jsonObj = new JSONObject(sysMess);
            logger.info("sysMess: " + sysMess);

            //电视猫312以下版本使用轮询 故存储在数据库
            String sysIndexKey = ConstantMess.SYSTEM_MESS_INDEX_KY_PREX + jsonObj.getString("userType");
            long scoreTime = Long.parseLong(jsonObj.getString("publishTime"));
            jedis.zadd(sysIndexKey, scoreTime, jsonObj.getString("uid"));
            String messKey = ConstantMess.SYSTEM_MESS_KY_PREX + jsonObj.getString("uid");
            jedis.set(messKey, sysMess);

            JSONObject jsonResult = connectManage.getMessageJson();
            String type = jsonObj.get("type").toString();
            String typeName = "";
            int cmsType = Integer.parseInt(type);
            if (cmsType == 0) {
                typeName = "【公告系统】";
                jsonResult.put("type", ConstantMess.SYS_MESS);
            } else if (cmsType == 1) {
                typeName = "【升级消息】";
                jsonResult.put("type", ConstantMess.UPGRADE_MESS);
            } else if (cmsType == 2) {
                typeName = "【推送消息】";
                jsonResult.put("type", ConstantMess.PUSH_UPDATE_MESS);
            }
            jsonResult.put("content", jsonObj.get("content"));
            jsonResult.put("title", jsonObj.get("title"));
            jsonResult.put("displayTime", jsonObj.get("displayTime"));
            jsonResult.put("code", jsonObj.get("code"));
            jsonResult.put("publishTime", jsonObj.get("publishTime"));
            jsonResult.put("invalidTime", jsonObj.get("invalidTime"));
            JSONObject connectJson = connectManage.getConnectJson();
            connectJson.put("msgType", ConstantMess.MSGTYPE_CONTENT);
            connectJson.put("data", jsonResult);
            String message = connectJson.toString();
            logger.info("SYSMESS ==>" + message);

            Long timestamp = Long.parseLong(jsonObj.get("validTime").toString());
            Long nowtimestamp = System.currentTimeMillis();

            if (timestamp < nowtimestamp) { // 小于当前服务器系统时间立刻发给长连接
                connectManage.sendMessage(connectJson.toString(), "", ConstantMess.FORALLCLIENTS);
            } else { // >= 当前服务器系统时间，定时发送
                String date = new SimpleDateFormat("ss mm HH dd MM ?").format(new Date(timestamp));
                JobDataMap map = new JobDataMap();
                map.put("message", message);
                logger.info(typeName + " (定时时间为 ..." + date + "）");
                UUID uuid = UUID.randomUUID();
                QuartzManager.addJob(uuid.toString(), Myjob.class, date, map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public String getSystemMessage(String key) {
        return null;
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
