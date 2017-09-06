package com.lefore.ssm.dao;

import com.lefore.ssm.entity.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class GoodsDaoTest {

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void testQueryAll() {
        List<Goods> list = goodsDao.queryAll(0, 100);
        for (Goods goods : list) {
            System.out.println(goods);
        }
        System.out.println("--------------------------");
    }

    @Test
    public void testReduceNumber() {
        int result = goodsDao.reduceNumber(1000);
        System.out.println("testReduceNumber result:" + result);
        System.out.println("--------------------------");
    }

    @Test
    public void testBuyWithProcedure() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1000L);
        map.put("goodsId", 1000L);
        map.put("title", "抢购iPhone7");
        map.put("result", null);
        goodsDao.buyWithProcedure(map);
        //获取result
        System.out.println("testBugWithProcedure result:" + map.get("result"));
    }
}
