package com.lefore.dubbox;

import com.alibaba.fastjson.JSONObject;
import com.lefore.dubbox.api.UserService;
import com.lefore.dubbox.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * author: lefore
 * date: 2017/8/27
 * email: 862080515@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class ConsumerTest {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        try {
            User user = userService.getUserByPhone("1233523452");
            LOG.info(JSONObject.toJSONString(user));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}
