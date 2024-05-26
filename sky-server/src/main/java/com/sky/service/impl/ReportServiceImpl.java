package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-26 21:13
 * @description:
 **/
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 统计营业额报表
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        log.info("开始统计营业额报表, 从 {} 到 {}", begin, end);

        // 当前集合用于存放从 begin到 end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!end.equals(begin)) {
            // 日期计算, 计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 将日期用 ',' 连接成字符串
        String dateListStr = StringUtils.join(dateList, ",");

        // 当前集合用于存放从 begin到 end范围内的每天的营业额
        List<Double> totalAmount = new ArrayList<>();
        // 查询日期的营业额
        dateList.forEach(date -> {
            // 查询指定日期的营业额, 营业额是指: 状态为"已完成"的订单金额合计
            // 设置当天时分秒最小 例 : date = 2022年10月10日, 则设置为 2022年10月10日 00:00:00
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            // 设置当天时分秒最大 例 : date = 2022年10月10日, 则设置为 2022年10月10日 23:59:59
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            // SELECT SUM(amount) FROM orders WHERE order_time > ? AND order_time < ? AND status = 5
            Double amount = orderMapper.getSumAmountByMap(map);
            amount = amount == null ? 0.0 : amount;
            // 将每天营业额加入集合
            totalAmount.add(amount);
        });
        // 将日期用 ',' 连接成字符串
        String amountListStr = StringUtils.join(totalAmount, ",");

        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(amountListStr)
                .build();
    }
}
