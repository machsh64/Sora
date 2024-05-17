package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-17 22:32
 * @description:
 **/
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status
     */
    @Override
    public void setStatus(Integer status) {
        log.info("店铺营业状态: {}", status == 1 ? "营业中" : "打垟中");

        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(KEY, status);
    }

    /**
     * 获取店铺营业状态
     *
     * @return
     */
    @Override
    public Integer getStatus() {
        log.info("店铺营业状态: {}", redisTemplate.opsForValue().get(KEY));

        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        if (shopStatus != null)
            return shopStatus;
        throw new RuntimeException("没有查询到数据");
    }
}
