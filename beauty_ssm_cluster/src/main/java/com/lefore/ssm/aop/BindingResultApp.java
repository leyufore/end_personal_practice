package com.lefore.ssm.aop;

import com.lefore.ssm.dto.BaseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * 采用AOP的方式处理参数问题。
 * <p>
 * author: lefore
 * date: 2017/8/24
 * email: 862080515@qq.com
 */
@Component
@Aspect
public class BindingResultApp {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义在 web 包里的任意方法的执行切面方法
     */
    @Pointcut("execution(* com.lefore.ssm.web.*.*(..))")
    public void aopMethod() {
    }

    @Around("aopMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LOG.info("before method invoking");
        BindingResult bindingResult = null;
        // ProceedingJoinPoint.getArgs 获取方法参数
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BindingResult) {
                bindingResult = (BindingResult) arg;
            }
        }
        if (bindingResult != null) {
            if (bindingResult.hasErrors()) {
                String errorInfo = "[" + bindingResult.getFieldError() + "]" + bindingResult.getFieldError().getDefaultMessage();
                return new BaseResult<Object>(false, errorInfo);
            }
        }
        // 调用目标方法
        return joinPoint.proceed();
    }
}
