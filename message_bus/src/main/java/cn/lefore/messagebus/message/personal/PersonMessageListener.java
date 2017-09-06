package cn.lefore.messagebus.message.personal;

import cn.lefore.messagebus.dao.personal.IPersonalMessageJedisDao;
import cn.lefore.messagebus.service.personal.IPersonalMessageService;

/**
 * author: lefore
 * date: 2017/8/7
 * email: 862080515@qq.com
 */
public class PersonMessageListener {

    private IPersonalMessageService personalMessageService;

    public void listenCollection(Object collMess) {
        personalMessageService.syncMessagePool(collMess);
    }

    public void listenStar(Object starMess) {
        personalMessageService.star2Uid(starMess);
    }

    public IPersonalMessageService getPersonalMessageService() {
        return personalMessageService;
    }

    public void setPersonalMessageService(IPersonalMessageService personalMessageService) {
        this.personalMessageService = personalMessageService;
    }
}
