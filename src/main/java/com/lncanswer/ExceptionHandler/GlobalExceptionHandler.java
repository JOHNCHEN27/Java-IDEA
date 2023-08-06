package com.lncanswer.ExceptionHandler;

import com.lncanswer.entitly.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationSupport;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class}) //对哪些类进行异常处理
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<String> ex (Exception e){
        e.printStackTrace();
        log.info(e.getMessage());

        if(e.getMessage().contains("Duplicate entry")){
            //利用字符串 分隔符split 空白作为分隔符 将信息封装成一个数组
           String [] split =  e.getMessage().split(" ");
           log.info("split= {}",split);
           String msg  = split[9] + "已存在";
           return Result.error(msg);
        }
        if (e.getMessage().contains("当前分类")){
            return  Result.error(e.getMessage());

        }
        return Result.error("服务器错误");
    }
}
