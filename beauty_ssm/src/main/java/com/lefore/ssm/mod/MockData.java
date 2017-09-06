package com.lefore.ssm.mod;

import com.lefore.ssm.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: lefore
 * date: 2017/8/14
 * email: 862080515@qq.com
 */
public class MockData {

    public static List<User> mockUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUserName("name" + i);
            user.setUserId(i);
            user.setUserPhone(i);
            user.setCreateTime(new Date());
            user.setScore(i);
            userList.add(user);
        }
        return userList;
    }
}
