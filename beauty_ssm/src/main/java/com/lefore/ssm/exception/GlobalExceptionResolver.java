package com.lefore.ssm.exception;

import com.alibaba.fastjson.JSON;
import com.lefore.ssm.dto.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 错误信息统一处理
 * 对未处理的错误信息做一个统一处理
 * <p>
 * author: lefore
 * date: 2017/8/24
 * email: 862080515@qq.com
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.error("访问" + request.getRequestURI() + " 发生错误，错误信息:" + ex.getMessage());
        //这里有2种选择
        //跳转到定制化的错误页面
        /*ModelAndView error = new ModelAndView("error");
        error.addObject("exMsg", ex.getMessage());
		error.addObject("exType", ex.getClass().getSimpleName().replace("\"", "'"));*/
        //返回json格式的错误信息
        try {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            BaseResult<String> result = new BaseResult(false, ex.getMessage());
            writer.write(JSON.toJSONString(result));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOG.error("Exception:", e);
        }
        return null;
    }
}
