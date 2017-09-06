package com.lefore.ssm.web.user.exception;

import com.alibaba.fastjson.JSON;
import com.lefore.ssm.comment.dto.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
public class UserGlobalExceptionResolver implements HandlerExceptionResolver {

    private final Logger LOG = LoggerFactory.getLogger(UserGlobalExceptionResolver.class);

    @Override
    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.error("访问" + request.getRequestURI() + " 发生错误, 错误信息 :" + ex.getMessage());
        try {
            PrintWriter writer = response.getWriter();
            BaseResult<String> result = new BaseResult(false, ex.getMessage());
            writer.write(JSON.toJSONString(result));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
