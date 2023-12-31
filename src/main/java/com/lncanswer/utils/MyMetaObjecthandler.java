package com.lncanswer.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/*
实现MetaObjectHandle接口，对数据表字段进行增加和修改时进行填充相应字段
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    //不能使用MytaObjectHandler 获取sesiion中的值 通过thread local来获取
    @Override
    public void insertFill(MetaObject metaObject) {
        //插入时填充
        log.info("公告字段填充：....");
        log.info(metaObject.toString());
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
          //更新时填充
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
