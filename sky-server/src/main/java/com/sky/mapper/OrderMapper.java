package com.sky.mapper;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-23 21:50
 * @description:
 **/
@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     */
    void insert(Orders orders);
}
