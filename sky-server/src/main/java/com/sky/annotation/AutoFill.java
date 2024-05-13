package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-13 22:19
 * @description:  自定义注解, 用于标识某个方法需要进行功能字段自动填充处理
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 数据库操作类型: UPDATE  INSERT
    OperationType value();
}
