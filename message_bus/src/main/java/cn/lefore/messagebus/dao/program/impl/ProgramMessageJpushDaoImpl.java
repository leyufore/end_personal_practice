package cn.lefore.messagebus.dao.program.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.LogHelper;
import cn.lefore.messagebus.common.utils.PropertiesUtils;
import cn.lefore.messagebus.dao.program.IProgramMessageJpushDao;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class ProgramMessageJpushDaoImpl implements IProgramMessageJpushDao {

    private static Logger LOG = LogHelper.getLogger();
    private JedisPool jedisPool;

    /**
     * 定时任务 极光推送
     */
    public void JpushJobTimer() {
        LOG.info("START JPUSH TASK");
        createUidToSid(); //建立uid->n_sid
        groupByn_sid(); //根据n_sid 分组合并uid
        Jedis jedis = jedisPool.getResource();
        Set<String> sendList = jedis.keys("to*");
        if (sendList.size() != 0) {
            LOG.info("sendList is :" + sendList);
            for (String key : sendList) {
                String size = key.substring(2, 3); //其他几个节目有更新
                int length = Integer.parseInt(size);
                String updateSid = key.substring(key.length() - 12);//更新节目的Sid
                Set<String> uidSet = jedis.smembers(key);
                String programKey = ConstantMess.UPDATED_PROGRAM_MESS_KY_PREX + updateSid;
                String program = jedis.get(programKey);
                if (program == null) {
                    LOG.error("【节目不存在】 " + programKey + " 为空");
                }
                try {
                    JSONObject jsonProgram = new JSONObject(program);
                    String title = jsonProgram.getString("title");
                    LOG.info("uidSet :" + uidSet + " collect《" + title + "》 update，" + "other " + length + " update");
                    int i = 0;
                    ArrayList<String> list = new ArrayList<String>();
                    for (String uid : uidSet) {
                        list.add(uid);
                        i++;
                        if (i > 900) {
                            System.out.println("updateProgram_sendUpdateMessageToSome_" + System.currentTimeMillis());
                            i = 0;
                            sendUpdateMessageToSome(list, title, length, updateSid);
                            list.clear();
                        }
                    }
                /*list.add("zhoulingyu"); //测试ios 别名为zhoulingyu*/
                    sendUpdateMessageToSome(list, title, length, updateSid);  //todo:升级前定要取消注释！！！！
                    list.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        jedis.close();
    }

    /**
     * 建立 uid -> n_sid 表
     */
    private void createUidToSid() {
        Jedis jedis = jedisPool.getResource();
        List<String> sidList = jedis.lrange("new", 0, -1);
        if (sidList.size() != 0) {
            LOG.info("UPDATE SID = " + sidList);
            int size = sidList.size();
            jedis.del("new"); // 删除 new list
            for (int j = 0; j < size; j++) {
                String sidUpdated = sidList.get(j);
                Set<String> uidSet = jedis.zrange(ConstantMess.SID_COLLECT_MESS_INDEX_KY_PREX + sidUpdated, 0, -1);
                for (String uid : uidSet) {
                    if (uid.length() > 9) {
                        continue;
                    }
                    String jkey = "jpush" + uid;
                    if (jedis.exists(jkey)) {
                        String n_sid = jedis.get(jkey);
                        String n = n_sid.substring(0, 1);
                        String oldsid = n_sid.substring(2);
                        int length = Integer.parseInt(n);
                        length += 1;
                        jedis.set(jkey, length + "_" + oldsid);
                    } else {
                        jedis.set(jkey, "0_" + sidUpdated);
                    }
                }
            }
        }
        jedis.close();
    }


    /**
     * 根据n_sid进行分组，以便于极光批量推送
     */
    public void groupByn_sid() {
        Jedis jedis = jedisPool.getResource();
        Set<String> uidList = jedis.keys("jpush*");
        for (String key : uidList) {
            String n_sid = jedis.get(key);
            if (n_sid == null) {
                return;
            }
            String uid = key.substring(5);
            jedis.del(key); //把 jpush+uid 的键删除
            jedis.sadd("to" + n_sid, uid);  //建立 合并之后的分组
            jedis.expire("to" + n_sid, 14400); //设置过期时间为4h
        }
        jedis.close();
    }

    /**
     * 批量发送
     *
     * @param uidListParm
     * @param title
     * @param length
     * @param sidParm
     */
    private void sendUpdateMessageToSome(ArrayList<String> uidListParm, String title, int length, String sidParm) {
        Audience audience_jpushParam = Audience.alias(uidListParm);

        String alert_jpushParam = "";
        Map<String, String> extras_jpushParamIOS = new HashMap<String, String>();
        extras_jpushParamIOS.put("sid", sidParm);
        Map<String, String> extras_jpushParamAndroid = new HashMap<String, String>();
        extras_jpushParamAndroid.put("sid", sidParm);
        if (length == 0) {
            alert_jpushParam = "您收藏的《" + title + "》有更新了，赶紧点开看看吧";
            extras_jpushParamIOS.put("type", "1");
            extras_jpushParamAndroid.put("type", "2");
        } else {
            alert_jpushParam = "您收藏的《" + title + "》和其他" + length + "个节目有更新，赶紧点开看看吧";
            extras_jpushParamIOS.put("type", "2");
            extras_jpushParamAndroid.put("type", "2");
        }

        LOG.info("Jpush == >" + alert_jpushParam + "   type :" + extras_jpushParamIOS.get("type"));
        PushPayload pushPayload = buildPushObject_byIOS(audience_jpushParam, alert_jpushParam, extras_jpushParamIOS);
        PushPayload pushPayload_android = buildPushObject_byAndroid(audience_jpushParam, alert_jpushParam, extras_jpushParamAndroid);

        sendPushPayLoad(pushPayload);
        sendPushPayLoad(pushPayload_android);
        LOG.info("END Jpush Task");
    }

    /**
     * android 使用 自定义消息 进行极光推送
     * 高级版本 1.2.0
     *
     * @param extrasParm
     * @return
     */
    private PushPayload buildPushObject_byAndroid(Audience audienceParam, String alertParm, Map<String, String> extrasParm) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(audienceParam)
                .setMessage(Message.newBuilder()
                        .addExtras(extrasParm)
                        .setMsgContent(alertParm)
                        .build())
                .build();
    }

    /**
     * ios 使用 通告 进行极光推送
     *
     * @param audienceParm
     * @param alertParm
     * @param extrasParm
     * @return
     */
    private PushPayload buildPushObject_byIOS(Audience audienceParm, String alertParm, Map<String, String> extrasParm) {
        boolean jpush_env = Boolean.parseBoolean(PropertiesUtils.getProperty("JPUSH_ENV"));
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(audienceParm)
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(
                                IosNotification.newBuilder()    //ios 1.1.0 && 1.2.0
                                        .setAlert(alertParm)
                                        .setBadge(1)
                                        .setSound("default")
                                        .addExtras(extrasParm)
                                        .build())
                        .addPlatformNotification(
                                AndroidNotification.newBuilder()    //android 1.1.0
                                        .setAlert(alertParm)
                                        .setTitle(alertParm)
                                        .addExtras(extrasParm)
                                        .build())
                        .build())
                .setOptions(Options.newBuilder() //推送环境
                        .setApnsProduction(jpush_env)
                        .build())
                .build();
    }

    /**
     * 推送极光消息
     *
     * @param pushPayload
     */
    private void sendPushPayLoad(PushPayload pushPayload) {
        ClientConfig.getInstance().setMaxRetryTimes(3);
        JPushClient jPushClient = new JPushClient(ConstantMess.JPUSH_MASTER_SECRET, ConstantMess.JPUSH_APPKEY, null, ClientConfig.getInstance());

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = pushPayload;

        try {
            PushResult result = jPushClient.sendPush(payload);
            LOG.info("Got result - " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            System.out.println("Error Message: " + e.getErrorMessage());
        }
        System.out.println("2016-06-28 return  success");
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // -------------------------// 测试～和ios联调-------------------------------------------
    /*
    * 测试消息，极光推送接口发送消息 2016-01-26
    * */
    private void sendTestMessage(String updateMess) {
        //JPushClient jpushClient = new JPushClient("5b0a4dc62eb362266e9a0afa", "46c80b6695cb2dd1eeed3520", 3); //old appKey 2016-04-21
        JPushClient jpushClient = new JPushClient(ConstantMess.JPUSH_MASTER_SECRET, ConstantMess.JPUSH_APPKEY, 3);//new appKey 2016-04-21

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);
            // LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            //LOG.error("Connection error, should retry later", e);
            System.out.println("Connection error, should retry later");
            System.out.println(e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            //LOG.error("Should review the error, and fix the request", e);
            System.out.println("Should review the error, and fix the request");
            System.out.println(e);
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }

    }

    /*
    * Audience: 推送受众
    *   alias：推送给个人
    *   tag：推送给群组
    *   Audience.all()：广播--推送给所有人
    * alert：推送标题
    * Extras：附加参数
    *   type = 1，针对用户进行个推的，【跳到视频详情页】，（收藏的某个节目有更新）
    *   type = 0，应该是广播，【跳转到每日推送 日报页】，（）
    * */
    private PushPayload buildPushObject_all_all_alert() {
        // jpush的附加参数-和客户端约定好的[type:0|1]
        Map<String, String> stringStringHashMap = new HashMap<String, String>();
        stringStringHashMap.put("sid", "3fx0ab9xd4a1");
        // type=0
        stringStringHashMap.put("type", "0");//可变参数
        // type=1
//        stringStringHashMap.put("type", "1");//可变参数

        boolean jpush_env = Boolean.parseBoolean(PropertiesUtils.getProperty("JPUSH_ENV"));

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())

                // type=0
                .setAudience(Audience.all())
                // type=1
//                .setAudience(Audience.alias("31859208"))//可变参数
//                .setAudience(Audience.tag_and("tag1", "tag_all"))


                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder() //可变参数

                                // type=1
//                                .setAlert("type = 1，是用户收藏的电视剧有更新的，然后针对用户alias进行个推，然后跳到视频详情页的")//可变参数
                                // type=0
                                .setAlert("type = 0，是广播，【跳转到每日推送 日报页】")//可变参数

                                .setBadge(1)
                                .setSound("default")

//                                .addExtra("type", "0")

//                                .addExtra("type", "1")
//                                .addExtra("sid", "3fx0ab9xd4a1")

                                .addExtras(stringStringHashMap)
                                .build())
                        .build())
//                .setMessage(Message.content(MSG_CONTENT))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(jpush_env)
                        .build())
                .build();
    }
}
