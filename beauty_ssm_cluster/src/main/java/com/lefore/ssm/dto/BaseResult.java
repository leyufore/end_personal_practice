package com.lefore.ssm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * ajax 请求的返回类型封装JSON结果
 * <p>
 * author: lefore
 * date: 2017/8/24
 * email: 862080515@qq.com
 */

/**
 * @JsonInclude 标签 springmvc中的标注，是为了控制返回的json字符串显示哪些字段。这里的设置是为null的字段不显示
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = -4185151304730685014L;

    private boolean success;

    private T data;

    private String error;

    public BaseResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public BaseResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseResult [success=" + success + ", data=" + data + ", error=" + error + "]";
    }

}
