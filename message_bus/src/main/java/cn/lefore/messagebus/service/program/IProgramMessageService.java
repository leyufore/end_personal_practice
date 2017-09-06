package cn.lefore.messagebus.service.program;

import org.json.JSONObject;

/**
 * author: lefore
 * date: 2017/8/9
 * email: 862080515@qq.com
 */
public interface IProgramMessageService {

    void syncMessagePool(Object updateMess);

    void produceStarMess(Object obj);

    // [meits]接收@姚银锋的系统公告 2016-01-18
    void produceMetisSystemNoticeMess(Object obj);

    void produceTagMess(Object obj);

    //豆瓣标签列表调整
    void syncSidsInTagForDouban(JSONObject jo);

    //标签新增，变更消息生成，include：豆瓣 自定义
    void makeMess(JSONObject jo);
}
