package cn.lefore.messagebus.service.program.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.LogHelper;
import cn.lefore.messagebus.dao.program.IProgramMessageJedisDao;
import cn.lefore.messagebus.dao.program.IProgramMessageJpushDao;
import cn.lefore.messagebus.manager.ILongConnectManage;
import cn.lefore.messagebus.service.program.IProgramMessageService;
import cn.lefore.messagebus.service.tag.ITagMessageService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: lefore
 * date: 2017/8/9
 * email: 862080515@qq.com
 */
public class ProgramMessageServiceImpl implements IProgramMessageService {

    private static final Logger logger = LogHelper.getLogger();
    private ILongConnectManage connectManage;
    private IProgramMessageJedisDao programMessageJedisDao;
    private IProgramMessageJpushDao programMessageJpushDao;
    private ITagMessageService tagMessageService;

    private static JSONObject getMessToken() {
        JSONObject json = new JSONObject();
        try {
            json.put("sid", "");
            json.put("title", "");
            json.put("content", "");
            json.put("url", "");
            json.put("icon", "");
            json.put("displayTime", "5");
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            json.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 李俊CMS的“节目更新消息”的处理，ProgramMessageListener.java-->listenUpdate()
     *
     * @param updateMess
     */
    public void syncMessagePool(Object updateMess) {
//        try {
//            logger.info("# (programMessQueue)ProgramMessageServiceImpl-->syncMessagePool");
//
//            // 处理CMS李俊的消息  //更新前记得取消注释  @ 5 @
//            String buildedMess = translateMess(new JSONObject(updateMess.toString())).toString();
//        }
    }

    // 李俊CMS的“节目更新消息”的处理
    private JSONObject translateMess(JSONObject jsonUpdated) {
        JSONObject jsonResult = new JSONObject();
        String content = "";
        String num;
        try {
            num = jsonUpdated.getString("episode");
            String title = jsonUpdated.getString("title");
            jsonResult.put("sid", jsonUpdated.get("sid"));
            jsonResult.put("title", jsonUpdated.get("title"));
            if ("zongyi".equals(jsonUpdated.getString("contentType"))) {
                if (num.length() == 8) {
                    content = "《${title}》已更新至${num}期".replace("${title}", title).replace("${num}", num.substring(4));
                } else {
                    content = "《${title}》已更新至${num}集".replace("${title}", title).replace("${num}", num);
                }
                jsonResult.put("content", content);
                jsonResult.put("type", ConstantMess.ZONGYI_UPDATE_MESS); //type = 6 综艺更新消息
            } else if ("movie".equals(jsonUpdated.get("contentType"))) {
                content = "你预约的 影片《${title}》已经上线 啦".replace("${title}", title);
                jsonResult.put("content", content);
                jsonResult.put("type", ConstantMess.YUYUE_UPDATE_MESS); // type = 5 预约消息提醒
            } else {
                content = "《${title}》已更新至${num}集".replace("${title}", title).replace("${num}", num);
                jsonResult.put("content", content);
                jsonResult.put("type", ConstantMess.ZHUIJU_MESS); //type = 1 追剧消息
            }

            jsonResult.put("url", "");
            jsonResult.put("icon", jsonUpdated.getString("icon1"));
            jsonResult.put("displayTime", "5");

            Date now = new Date();
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            jsonResult.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    public void produceStarMess(Object obj) {

    }

    public void produceMetisSystemNoticeMess(Object obj) {

    }

    public void produceTagMess(Object obj) {

    }

    public void syncSidsInTagForDouban(JSONObject jo) {

    }

    public void makeMess(JSONObject jo) {

    }
}
