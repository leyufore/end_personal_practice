package cn.lefore.messagebus.service.system.impl;

import cn.lefore.messagebus.dao.system.ISystemMessageJedisDao;
import cn.lefore.messagebus.service.system.ISystemMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by wenrule on 2017/7/29.
 */
public class SystemMessageServiceImpl implements ISystemMessageService {

    private ISystemMessageJedisDao systemMessageJedisDao;
    private RabbitTemplate rabbitTemplate;

    public void syncMessagePool(Object sysMess) {
        systemMessageJedisDao.save(sysMess.toString());
    }

    public ISystemMessageJedisDao getSystemMessageJedisDao() {
        return systemMessageJedisDao;
    }

    public void setSystemMessageJedisDao(ISystemMessageJedisDao systemMessageJedisDao) {
        this.systemMessageJedisDao = systemMessageJedisDao;
    }

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
