package com.lefore.ssm.service.impl;

import com.lefore.ssm.cache.RedisCache;
import com.lefore.ssm.dao.GoodsDao;
import com.lefore.ssm.dao.OrderDao;
import com.lefore.ssm.dao.UserDao;
import com.lefore.ssm.entity.Goods;
import com.lefore.ssm.entity.User;
import com.lefore.ssm.enums.ResultEnum;
import com.lefore.ssm.exception.BizException;
import com.lefore.ssm.service.GoodsService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisCache cache;

    @Override
    public List<Goods> getGoodsList(int offset, int limit) {
        String cache_key = RedisCache.CACHENAME + "|getGoodsList|" + offset + "|" + limit;
        List<Goods> result_cache = cache.getListCache(cache_key, Goods.class);
        if (result_cache != null) {
            LOG.info("get cache with key:" + cache_key);
        } else {
            // 缓存中没有再去数据库取，并插入缓存（缓存时间为 60 秒）
            result_cache = goodsDao.queryAll(offset, limit);
            cache.putListCacheWithExpireTime(cache_key, result_cache, RedisCache.CACHETIME);
            LOG.info("put cache with key:" + cache_key);
            return result_cache;
        }
        return result_cache;
    }

    /**
     * @param userPhone
     * @param goodsId
     * @param useProcedure 是否用存储过程提高并发能力
     * @Transactional 该标签用于开启事务，当操作过程当中出错，则进行事务回滚操作
     */
    @Transactional
    @Override
    public void buyGoods(long userPhone, long goodsId, boolean useProcedure) {
        // 用户校验
        User user = userDao.queryByPhone(userPhone);
        if (user == null) {
            throw new BizException(ResultEnum.INVALID_USER.getMsg());
        }
        if (useProcedure) {
            // 通过存储过程进行操作
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", user.getUserId());
            map.put("goodsId", goodsId);
            map.put("title", "抢购");
            map.put("result", null);
            goodsDao.buyWithProcedure(map);
            int result = MapUtils.getInteger(map, "result", -1);
            if (result <= 0) {
                // 买卖失败
                throw new BizException(ResultEnum.INNER_ERROR.getMsg());
            } else {
                // 买卖成功
                // 此时缓存中的数据不是最新的，需要对缓存进行清理（具体的缓存策略还是要根据具体需求制定）
                cache.deleteCacheWithPattern(RedisCache.CACHENAME + "|getGoodsList*");
                LOG.info("delete cache with key: getGoodsList*");
                return;
            }
        } else {
            int insertCount = orderDao.insertOrder(user.getUserId(), goodsId, "普通买卖");
            if (insertCount <= 0) {
                // 买卖失败
                throw new BizException(ResultEnum.DB_UPDATE_RESULT_ERROR.getMsg());
            } else {
                // 减库存
                int updateCount = goodsDao.reduceNumber(goodsId);
                if (updateCount <= 0) {
                    throw new BizException(ResultEnum.DB_UPDATE_RESULT_ERROR.getMsg());
                } else {
                    // 买卖成功
                    // 此时缓存中的数据不再是最新的，需要对缓存进行清理（具体的缓存策略还是要根据具体需求制定）
                    cache.deleteCacheWithPattern(RedisCache.CACHENAME + "|getGoodsList*");
                    LOG.info("delete cache with key: getGoodsList*");
                    return;
                }
            }
        }
    }
}
