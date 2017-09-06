package com.lefore.ssm.api.user.service.impl;


import com.lefore.ssm.api.user.entity.User;
import com.lefore.ssm.api.user.service.UserService;
import com.lefore.ssm.comment.util.cache.RedisCache;
import com.lefore.ssm.core.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisCache cache;

    @Override
    public List<User> getUserList(int offset, int limit) {
        String cache_key = RedisCache.CACHENAME + "|getUserList|" + offset + "|" + limit;
        //先去缓存中取
        List<User> result_cache = cache.getListCache(cache_key, User.class);
        if (result_cache == null) {
            //缓存中没有再去数据库中取，并插入缓存（缓存时间为60秒）
            result_cache = userDao.queryAll(offset, limit);
            // mock 数据
//            result_cache = MockData.mockUserList();
            cache.putListCacheWithExpireTime(cache_key, result_cache, RedisCache.CACHETIME);
            LOG.info("put cache with key:" + cache_key);
        } else {
            LOG.info("get cache with key:" + cache_key);
        }
        return result_cache;
    }

    @Override
    public User queryByPhone(long userPhone) {
        return userDao.queryByPhone(userPhone);
    }

    @Override
    public int addScoreBySyn(int score) {
        int result = userDao.addScore(score);
        return result;
    }

    @Override
    public int addScoreByAsy(int score) {
        return addScoreBySyn(score);
    }

}
