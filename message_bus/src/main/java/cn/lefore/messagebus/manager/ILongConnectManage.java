package cn.lefore.messagebus.manager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wenrule on 2017/7/31.
 */
public interface ILongConnectManage {
    /**
     * 调用API接口
     *
     * @param message 消息
     * @param targets 目标
     * @param type    类型
     * @return 返回值
     */
    String sendMessage(String message, String targets, int type);

    /**
     * 对消息系统提供的统一发长连接消息的接口
     *
     * @param message 消息
     * @param uidList 目标列表
     * @param type    类型
     * @throws org.json.JSONException
     */
    void sendMessageToIca(String message, List<String> uidList, int type) throws JSONException;


    /**
     * 将大量用户进行批量处理 每900个发一次
     *
     * @param updateMess 消息
     * @param uidList    目标列表
     * @param type       类型
     */
    void listInBatches(String updateMess, List<String> uidList, int type);


    /**
     * 构造统一的消息返回格式
     *
     * @return 格式化消息
     */
    JSONObject getMessageJson();

    JSONObject getConnectJson();
}