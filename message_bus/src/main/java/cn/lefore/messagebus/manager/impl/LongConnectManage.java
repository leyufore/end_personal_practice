package cn.lefore.messagebus.manager.impl;

import cn.lefore.messagebus.common.constants.ConstantMess;
import cn.lefore.messagebus.common.utils.*;
import cn.lefore.messagebus.manager.ILongConnectManage;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在个人学习环境中，该功能并不能进行测试
 * 故全部内容进行注释
 * Created by wenrule on 2017/7/31.
 */
public class LongConnectManage implements ILongConnectManage {

    private static final Logger logger = LogHelper.getLogger();

    public String sendMessage(String message, String targets, int type) {
        /**
        String API_addr = "";
        //dev 开发环境
        //String HOST = "";

        //test 测试环境 正式环境
        String HOST = "";

        switch (type) {
            case 1:
                API_addr = HOST + ConstantMess.API_FORUSERS;
                break;    //发给8位 用户
            case 2:
                API_addr = HOST + ConstantMess.API_FORCLIENTS;
                break;  //发给32位 用户
            case 3:
                API_addr = HOST + ConstantMess.API_FORONLINE;
                break;   //发给在线客户端
            case 4:
                API_addr = HOST + ConstantMess.API_FORALL;
                break;      //发给所有客户端
            case 5:
                API_addr = HOST + ConstantMess.API_FORALLBATCHES;
                break;//按策略发送
            case 6:
                API_addr = HOST + ConstantMess.API_FORDEVICEID;
                break;  //发给deviceid客户端
            case 7:
                API_addr = HOST + ConstantMess.API_FORUNLOGINONLINE; //发给未登录在线的客户端
                break;
            case 8:
                API_addr = HOST + ConstantMess.API_FORLOGINONLINE;  //发给已登录在线的客户端
                break;
        }
        logger.info("调用长连接API ： " + API_addr);
        String strResult = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_addr);
        try {
            String round = StringUtils.getRandomString(10);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = sdf.format(date);
            String timestamp = TimeUtils.getTimestamp(str);
            String signstr = ConstantMess.APP_SECRET + round + timestamp;
            String sign = SHA1.hex_sha1(signstr);

            JSONObject messageObj = new JSONObject(message);
            String messageBase64 = Coder.getBASE64(message);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("app_key", ConstantMess.APP_KEY));
            nameValuePairs.add(new BasicNameValuePair("app_secret", ConstantMess.APP_SECRET));
            nameValuePairs.add(new BasicNameValuePair("targets", targets));
            nameValuePairs.add(new BasicNameValuePair("round", round));
            nameValuePairs.add(new BasicNameValuePair("timestamp", timestamp));
            nameValuePairs.add(new BasicNameValuePair("sign", sign));
            nameValuePairs.add(new BasicNameValuePair("data", messageBase64));
            JSONObject messageObject = new JSONObject(messageObj.get("data").toString());
            if (messageObject.has("invalidTime")) {
                Long invalidTime = Long.parseLong(messageObject.get("invalidTime").toString()) / 1000;
                logger.info("消息失效时间 ：" + invalidTime);
                nameValuePairs.add(new BasicNameValuePair("failure_time", invalidTime.toString()));
            }
            nameValuePairs.add(new BasicNameValuePair("business_type", ConstantMess.BUSINESSTYPE));
            nameValuePairs.add(new BasicNameValuePair("receipt", "1"));
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String conResult = EntityUtils.toString(response.getEntity());
                logger.info(" 【response】: 200 OK ");
            } else {
                String err = response.getStatusLine().getStatusCode() + "";
                strResult += " 【response】发送失败: StatusCode " + err;
                logger.info(strResult);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
         **/
        return null;
    }

    public void sendMessageToIca(String message, List<String> uidList, int type) throws JSONException {
        /**
        if (uidList.size() != 0) {
            List<String> uid_8 = new ArrayList<String>();
            List<String> uid_32 = new ArrayList<String>();
            for (String uid : uidList) {
                if (uid.length() == 8) {
                    uid_8.add(uid);
                }

                if (uid.length() == 32) {
                    uid_32.add(uid);
                }
            }
            if (uid_8.size() != 0 && type == ConstantMess.FORUSERORCLIENT) {
                int flag = ConstantMess.FORUSERS;
                JSONObject jsonobj = new JSONObject(message);
                jsonobj.put("userType", ConstantMess.ACCOUNT);
                listInBatches(jsonobj.toString(), uid_8, flag); //批量发送长连接消息给指定8位用户
            }
            if (uid_32.size() != 0 && type == ConstantMess.FORUSERORCLIENT) {
                int flag = ConstantMess.FORCLIENTS;
                JSONObject jsonobj = new JSONObject(message);
                jsonobj.put("userType", ConstantMess.MAC);
                listInBatches(jsonobj.toString(), uid_32, flag); //批量发送长连接消息给指定32位客户端
            }
            if (type == ConstantMess.FORONLINE || type == ConstantMess.FORALLCLIENTS) {
                sendMessage(message, "", type); //向所有客户端发消息
            }
            if (type == ConstantMess.FORDEVICEID) {
                listInBatches(message, uidList, type); //批量发送长连接消息给指定deviceID客户端
            }
        }
         **/
    }

    public void listInBatches(String updateMess, List<String> uidList, int type) {
        /**
        try {
            StringBuffer buffer = new StringBuffer();
            int i = 0;
            for (String uid : uidList) {
                i++;
                buffer.append(uid + ",");
                if (i > 900) {
                    String targets = buffer.toString().substring(0, buffer.toString().length() - 1);
                    logger.info("[part] message :" + updateMess + "   uidList.size = 900");
                    sendMessage(updateMess, targets, type); //长连接发消息
                    buffer.delete(0, buffer.length() - 1);
                    i = 0;
                }
            }
            String targets = buffer.toString().substring(0, buffer.toString().length() - 1);
            logger.info("message :" + updateMess + "   uidList.size:" + uidList.size());
            sendMessage(updateMess, targets, type); //长连接发消息
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    public JSONObject getMessageJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("sid", "");
            json.put("title", "");
            json.put("content", "");
            json.put("url", "");
            json.put("icon", "");
            json.put("contentType", "");
            json.put("displayTime", "5");
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            json.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getConnectJson() {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("timestamp", System.currentTimeMillis());
            json.put("code", "lefore_message_bus");
            json.put("msgType", "command");
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
