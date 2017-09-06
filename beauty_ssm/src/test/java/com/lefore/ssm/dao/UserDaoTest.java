package com.lefore.ssm.dao;

import com.lefore.ssm.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testQueryId() {
        User user = userDao.queryByPhone(18768128888L);
        System.out.println(user);
        System.out.println("--------------------------");
    }

    @Test
    public void testQueryAll() {
        List<User> list = userDao.queryAll(0, 100);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testAddScore() {
        userDao.addScore(10);
        List<User> list = userDao.queryAll(0, 100);
        for (User user : list) {
            System.out.println(user);
        }
    }

}
