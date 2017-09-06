package com.lefore.ssm.comment.dto;

import java.io.Serializable;
import java.util.List;

/**
 * ajax 请求的返回类型封装JSON结果
 * <p>
 * 主要用于bootstrap table
 * <p>
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public class BootStrapTableResult<T> implements Serializable {

    private static final long serialVersionUID = -4185151304730685014L;


    private List<T> data;


    public BootStrapTableResult(List<T> data) {
        super();
        this.data = data;
    }


    public List<T> getData() {
        return data;
    }


    public void setData(List<T> data) {
        this.data = data;
    }


}
