package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-25 21:23
 * @description: 定时任务类, 定时处理订单状态
 **/
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单超时未支付
     */
    @Scheduled(cron = "0 0 * * * ?")  //每分钟触发一次
    public void processTimeOutOrder() {
        log.info("定时任务处理订单超时未支付: {}", LocalDateTime.now());

        // select * form orders where status = ? and order_time < (当前时间 - 15分钟)
        List<Orders> ordersList = orderMapper.getTimeOutOrder(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if (ordersList != null && ordersList.size() > 0) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时, 自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理订单超时商家未处理
     */
    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨一点触发一次
    public void processDeliveryOrder(){
        log.info("定时任务处理订单超时商家未处理: {}", LocalDateTime.now());

        // select * from orders where status = ? and order_time < (当前时间 - 30分钟)
        List<Orders> ordersList = orderMapper.getTimeOutOrder(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-30));

        if (ordersList != null && ordersList.size() > 0) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.COMPLETED);
                order.setCancelReason("订单已完成, 状态修改");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
}
