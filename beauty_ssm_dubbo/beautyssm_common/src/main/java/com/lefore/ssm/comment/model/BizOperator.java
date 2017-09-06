package com.lefore.ssm.comment.model;

/**
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public class BizOperator {

    private String operator;
    private long goodsId;

    public BizOperator(String operator, long goodsId) {
        this.operator = operator;
        this.goodsId = goodsId;
    }

    public BizOperator() {
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
