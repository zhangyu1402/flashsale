package com.zhangyu.seckill.handler;

import com.zhangyu.seckill.dto.Response;
import com.zhangyu.seckill.dto.SeckillResult;
import com.zhangyu.seckill.enums.SeckillStateEnum;
import com.zhangyu.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(value = SeckillException.class)
    public Response<SeckillResult> seckillExceptionHandler(HttpServletRequest httpServletRequest, SeckillException e) {
        System.err.println(e.getSeckillStateEnum()+" "+e.getMessage());
        logger.error(e.getMessage(), e);
        SeckillResult execution = new SeckillResult(e.getSeckillStateEnum());
        return new Response<>(false, execution);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response<SeckillResult> exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        System.err.println(e.getMessage());
        logger.error(e.getMessage(), e);
        SeckillResult execution = new SeckillResult(SeckillStateEnum.INNER_ERROR);
        return new Response<>(false, execution);
    }

}
