package com.sky.service;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-17 22:32
 * @description:
 **/
public interface ShopService {

    /**
     * 设置店铺状态
     * @param status
     */
    void setStatus(Integer status);

    /**
     * 查询店铺营业状态
     * @return
     */
    Integer getStatus();
}
