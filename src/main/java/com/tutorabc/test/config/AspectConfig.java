package com.tutorabc.test.config;

import com.vipabc.basic.common.util.exception.CoreException;
import com.vipabc.basic.common.util.exception.SystemErrorCodeEnum;
import com.vipabc.basic.common.web.vo.ResponseVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by lykosliu on 2017/3/22.
 */
@Aspect
@Configuration
@ComponentScan("com.vipabc.basic.common.util.exception")
public class AspectConfig {

    @Pointcut("execution(* com.tutorabc.test.controller.*Controller.*(..))")
    public void executeController(){}

    /**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("executeController()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp){
        Object result = null;
        try {
            result = pjp.proceed();
        }catch(IllegalArgumentException ia){
            result = new ResponseVO(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), ia.getMessage());
        }catch(CoreException qe){
            result = new ResponseVO(qe.getCode(), qe.getMessage());
        }catch(Exception t){
            result = new ResponseVO(SystemErrorCodeEnum.SYSTEM_ERROR.getCode(), SystemErrorCodeEnum.SYSTEM_ERROR.getMessage()+(t.getMessage()==null?"":t.getMessage()));
        }catch(Throwable t){
            result = new ResponseVO(SystemErrorCodeEnum.SYSTEM_ERROR.getCode(),SystemErrorCodeEnum.SYSTEM_ERROR.getMessage()+(t.getMessage()==null?"":t.getMessage()));
        }
        return result;
    }


    @Bean("messageSource")
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(1);
        messageSource.setBasename("classpath:/i18n/message");
        return messageSource;
    }

}
