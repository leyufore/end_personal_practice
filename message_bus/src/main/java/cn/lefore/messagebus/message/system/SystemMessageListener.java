package cn.lefore.messagebus.message.system;

import cn.lefore.messagebus.service.system.ISystemMessageService;

/**
 * author: lefore
 * date: 2017/7/31
 * email: 862080515@qq.com
 */
public class SystemMessageListener {

    private ISystemMessageService systemMessageService;

    /**
     * CMS 发的电视猫系统公告
     *
     * @param sysMess
     */
    public void listen(Object sysMess) {
        systemMessageService.syncMessagePool(sysMess);
    }

    public ISystemMessageService getSystemMessageService() {
        return systemMessageService;
    }

    public void setSystemMessageService(ISystemMessageService systemMessageService) {
        this.systemMessageService = systemMessageService;
    }
}
