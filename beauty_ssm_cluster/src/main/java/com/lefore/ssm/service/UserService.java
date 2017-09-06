package com.lefore.ssm.service;

import com.lefore.ssm.entity.User;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/12
 * email: 862080515@qq.com
 */
public interface UserService {

    List<User> getUserList(int offset, int limit);

}
