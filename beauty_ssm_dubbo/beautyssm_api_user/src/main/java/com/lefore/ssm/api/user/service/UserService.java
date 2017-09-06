package com.lefore.ssm.api.user.service;

import com.lefore.ssm.api.user.entity.User;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public interface UserService {

    List<User> getUserList(int offset, int limit);

    User queryByPhone(long userPhone);

    /**
     * 用于消费端同步调用（需要在dubbo-consumer.xml中配置async参数）
     *
     * @param score
     * @return
     */
    int addScoreBySyn(int score);

    /**
     * 用于消费端异步调用（需要在dubbo-consumer.xml中配置async参数）
     *
     * @param score
     * @return
     */
    int addScoreByAsy(int score);
}
