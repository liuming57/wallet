package com.example.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author 10785
 */
@Component
public class MybatisplusHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("creationTime", new Date(), metaObject);
        this.setFieldValByName("objectVersion", 1L, metaObject);
        this.setFieldValByName("lastUpdateTime", new Date(), metaObject);
        this.setFieldValByName("createdBy", -1L, metaObject);
        this.setFieldValByName("lastUpdateBy", -1L, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastUpdateTime", new Date(), metaObject);
        this.setFieldValByName("objectVersion", (long) metaObject.getValue("objectVersion") + 1, metaObject);
    }

}