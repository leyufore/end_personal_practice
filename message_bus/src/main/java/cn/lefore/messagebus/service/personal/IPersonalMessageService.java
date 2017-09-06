package cn.lefore.messagebus.service.personal;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public interface IPersonalMessageService {

    void syncMessagePool(Object collMess);

    void star2Uid(Object starMess);
}
