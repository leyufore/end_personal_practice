package com.lefore.ssm.dao;

import com.lefore.ssm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/12
 * email: 862080515@qq.com
 */
public interface UserDao {

    /**
     * 根据手机号码查询用户对象
     *
     * @param userPhone
     * @return
     */
    User queryByPhone(long userPhone);

    /**
     * 根据偏移量查询用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<User> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 增加积分
     *
     * @param add
     */
    void addScore(@Param("add") int add);


}
