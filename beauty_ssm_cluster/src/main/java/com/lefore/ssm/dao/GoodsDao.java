package com.lefore.ssm.dao;

import com.lefore.ssm.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
public interface GoodsDao {

    /**
     * 根据偏移量查询可用商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Goods> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 商品减库存
     *
     * @param goodsId
     * @return 如果更新行数大于1, 表示更新的行数
     */
    int reduceNumber(long goodsId);

    /**
     * 使用存储过程执行抢购
     * <p>
     * 能提升并发性的原因：
     * 1、减少多个sql语句执行来回的网络延迟
     * 2、通过mysql自身的事务提升效率
     *
     * 使用 mysql 存储过程方法：
     * 1、在 mysql 中创建存储过程 Procedure 。在 mysql 客户端使用 source execute_buy.mysql
     * 2、运行该方法会相应调用已定义好的存储过程
     * @param paramMap
     */
    void buyWithProcedure(Map<String, Object> paramMap);
}
