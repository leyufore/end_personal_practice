package com.lefore.ssm.api.goods.service;

import com.lefore.ssm.api.goods.entity.Goods;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public interface GoodsService {

    /**
     * 根据偏移量查询可用商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Goods> getGoodsList(int offset, int limit);

    /**
     * 商品购买
     *
     * @param userPhone
     * @param goodsId
     * @param useProcedure 是否用存储过程提高并发能力
     */
    void buyGoods(long userPhone, long goodsId, boolean useProcedure);

    /**
     * 仅仅是为了测试下分布式事务
     *
     * @param goodsid
     */
    void testDistributedTransaction(long goodsid);
}