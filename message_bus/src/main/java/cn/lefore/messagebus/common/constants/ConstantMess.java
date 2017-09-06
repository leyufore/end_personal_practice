package cn.lefore.messagebus.common.constants;

/**
 * author: lefore
 * date: 2017/7/31
 * email: 862080515@qq.com
 */
public class ConstantMess {
    public static final String JPUSH_APPKEY = "";
    public static final String JPUSH_MASTER_SECRET = "";

    public static final String MSGTYPE_CONTENT = "";

    //keep connection
    public static final String APP_KEY = "=";
    public static final String APP_SECRET = "";
    public static final String BUSINESSTYPE = "";

    //keep connection API
    public static final String API_FORUSERS = "Api/PushForSpecificUsers/";      //指定 8 位帐号 来推送
    public static final String API_FORCLIENTS = "Api/PushForSpecificClients/"; //指定 32位业务号来推送
    public static final String API_FORONLINE = "Api/PushForOnlineClients/";
    public static final String API_FORALL = "Api/BroadcastInNoTime/";
    public static final String API_FORALLBATCHES = "Api/BroadcastInBatches/";
    public static final String API_FORDEVICEID = "Api/PushForSpecificClients/";  //向指定 deviceID 来推送

    public static final String API_FORUNLOGINONLINE = "Api/multicastForUserOffline/"; // 7 所有已登陆用户的在线设备
    public static final String API_FORLOGINONLINE = "Api/multicastForUserOnline/";    // 8 所有未登陆用户的在线设备

    /**
     * 为8位 和 32位 区分消息
     */
    public static final int ACCOUNT = 1;
    public static final int MAC = 2;

    // APT type
    /**
     * 给指定8位用户发
     */
    public static final int FORUSERS = 1;
    /**
     * 给指定32位客户端发
     */
    public static final int FORCLIENTS = 2;
    /**
     * 给在线所有设备发
     */
    public static final int FORONLINE = 3;
    /**
     * 给所有设备发
     */
    public static final int FORALLCLIENTS = 4;
    /**
     * 按策略发
     */
    public static final int FORBATCHES = 5;
    /**
     * 给指定deviceID设备发
     */
    public static final int FORDEVICEID = 6;
    /**
     * 给指定未登录的在线设备发
     */
    public static final int FORUNLOGINONLINE = 7;
    /**
     * 给指定已登录的在线设备发
     */
    public static final int FORLOGINONLINE = 8;
    /**
     * 给指定8位用户或32位客户端发
     */
    public static final int FORUSERORCLIENT = 12;

    //keep connection end

    public static final String OP_DEL = "del";
    public static final String OP_ADD = "add";

    // system message
    public static final String SYSTEM_MESS_INDEX_KY_PREX = "sm:userType:";
    public static final String SYSTEM_MESS_KY_PREX = "sm:uid:";
    // persion message
    public static final String USER_COLLECT_MESS_INDEX_KY_PREX = "u:collect:";
    public static final String SID_COLLECT_MESS_INDEX_KY_PREX = "sid:uid:collect:";//pht,sid被哪几个uid收藏了
    public static final String TV_COLLECT_MESS_INDEX_KY_PREX = "t:collect:";
    public static final String UPDATED_PROGRAM_MESS_KY_PREX = "n:sid:";
    public static final String BUFFER_KEY = "collect:buffer:temp";
    // star message
    public static final String STAR_PREX = "star2uid:";
    public static final String STAR_MESS_PREX = "uid2starmess:";


    // message source
    public static final String FROM_TV = "tv";
    public static final String FROM_MOBILE = "user";

    // message type
    /**
     * 追剧更新
     */
    public static final int ZHUIJU_MESS = 1;
    /**
     * 升级提示
     */
    public static final int UPGRADE_MESS = 2;
    /**
     * 系统公告
     */
    public static final int SYS_MESS = 3;
    /**
     * 消息推送
     */
    public static final int PUSH_UPDATE_MESS = 4;
    /**
     * 预约消息提醒
     */
    public static final int YUYUE_UPDATE_MESS = 5;
    /**
     * 综艺更新
     */
    public static final int ZONGYI_UPDATE_MESS = 6;
    /**
     * 订阅标签
     */
    public static final int TAG_UPDATE_MESS = 7;
    /**
     * 明星更新
     */
    public static final int STAR_UPDATE_MESS = 8;
}
