package org.xhxin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhxin.common.ResultMsg;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResultMsg<String> defaultErrorView(Exception e){
        log.error("ExceptionHandler---",e);
        ResultMsg<String> msg = new ResultMsg<>();
        msg.setCode(ResultMsg.ERROR);
        msg.setMessage("服务器繁忙");
        return msg;
    }

}