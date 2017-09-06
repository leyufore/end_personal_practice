package cn.lefore.messagebus.service.tag;

/**
 * author: lefore
 * date: 2017/8/9
 * email: 862080515@qq.com
 */
public interface ITagMessageService {

    void tag2Uid(Object o);

    void custTag2Sid(Object o);

    void removeSidInTag(String sid, String tag, String type);

    void addSidInTag(String sid, String tag, String type);
}
