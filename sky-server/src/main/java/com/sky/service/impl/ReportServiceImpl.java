package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Employee;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

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

    /**
     * 统计用户报表
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        log.info("开始统计用户报表, 从 {} 到 {}", begin, end);

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
        // 统计每天的新用户数 和 总用户数
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        dateList.forEach(date -> {
            // 查询指定日期的营业额, 营业额是指: 状态为"已完成"的订单金额合计
            // 设置当天时分秒最小 例 : date = 2022年10月10日, 则设置为 2022年10月10日 00:00:00
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            // 设置当天时分秒最大 例 : date = 2022年10月10日, 则设置为 2022年10月10日 23:59:59
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);

            // select count(id) from user where creat_time < ?
            // 查询指定日期的总用户数
            Integer allUserC = userMapper.getUserCByMap(map);
            // select count(id) from user where creat_time < ? and creat_time > ?
            // 查询指定日期的新用户数
            map.put("begin", beginTime);
            Integer newUserC = userMapper.getUserCByMap(map);

            allUserC = allUserC == null ? 0 : allUserC;
            newUserC = newUserC == null ? 0 : newUserC;

            totalUserList.add(allUserC);
            newUserList.add(newUserC);
        });

        // 将每天的总用户数 用 ',' 连接成字符串
        String totalUserListStr = StringUtils.join(totalUserList, ",");
        // 将每天的新用户数 用 ',' 连接成字符串
        String newUserListStr = StringUtils.join(newUserList, ",");

        return UserReportVO.builder()
                .dateList(dateListStr)
                .totalUserList(totalUserListStr)
                .newUserList(newUserListStr)
                .build();
    }

    /**
     * 统计订单报表
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        log.info("开始统计订单报表, 从 {} 到 {}", begin, end);

        // 当前集合用于存放从 begin到 end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);
        while (!end.equals(begin)) {
            // 日期计算, 计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 将日期用 ',' 连接成字符串
        String dateListStr = StringUtils.join(dateList, ",");

        // 统计每天的总订单数
        List<Integer> orderCList = new ArrayList<>();
        // 存放每天的有效订单数
        List<Integer> validOrderCList = new ArrayList<>();

        // 遍历dateList集合, 查询每天的有效订单数和订单总数
        dateList.forEach(date -> {
            // 查询指定日期的营业额, 营业额是指: 状态为"已完成"的订单金额合计
            // 设置当天时分秒最小 例 : date = 2022年10月10日, 则设置为 2022年10月10日 00:00:00
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            // 设置当天时分秒最大 例 : date = 2022年10月10日, 则设置为 2022年10月10日 23:59:59
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 查询每天的订单总数 select count(id) form orders where order_time < ? and order_time > ?
            Integer orderC = getCByMap(beginTime, endTime, null);
            // 查询每天有效的订单总数 select count(id) form orders where order_time < ? and order_time > ? and status = 5
            Integer validOrderC = getCByMap(beginTime, endTime, Orders.COMPLETED);

            orderC = orderC == null ? 0 : orderC;
            validOrderC = validOrderC == null ? 0 : validOrderC;

            orderCList.add(orderC);
            validOrderCList.add(validOrderC);
        });

        // 将每天的总订单数 用 ',' 连接成字符串
        String orderCListStr = StringUtils.join(orderCList, ",");
        // 将每天的有效订单数 用 ',' 连接成字符串
        String validOrderCListStr = StringUtils.join(validOrderCList, ",");

        // 计算时间区间内订房单数总和
        Integer totalOrderC = orderCList.stream().reduce(Integer::sum).get();
        // 计算时间额你有效订单数总和
        Integer validOrderC = validOrderCList.stream().reduce(Integer::sum).get();

        // 计算订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderC != 0) {
            orderCompletionRate = validOrderC.doubleValue() / totalOrderC;
        }

        return OrderReportVO.builder()
                .dateList(dateListStr)
                .orderCountList(orderCListStr)
                .validOrderCountList(validOrderCListStr)
                .totalOrderCount(totalOrderC)
                .validOrderCount(validOrderC)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 获取销量前10的菜品
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        log.info("统计期间销量前十, 从 {} 到 {}", begin, end);

        // select od.name, sum(od.number) AS number from order_detail od
        // where od.order_id = o.id and o.id = 5 and o.order_time > ? and o.order_time < ?
        // group by od.name
        // order by number desc
        // limit 0,10

        // 获取销量前十的商品
        List<GoodsSalesDTO> goodsSalesDTOS = orderMapper.getTop10(begin, end);

        // 获取将销量前十的名称集合 并将其加入Str以","分割
        List<String> nameList = goodsSalesDTOS.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameListStr = StringUtils.join(nameList, ",");
        // 获取将销量前十的销量集合 并将其加入Str以","分割
        List<Integer> numberList = goodsSalesDTOS.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberListStr = StringUtils.join(numberList, ",");

        // 返回结果数据
        return SalesTop10ReportVO.builder()
                .nameList(nameListStr)
                .numberList(numberListStr)
                .build();
    }

    /**
     * 统计用户报表
     *
     * @param begin
     * @param end
     * @param status
     * @return
     */
    private Integer getCByMap(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap();
        map.put("end", end);
        map.put("begin", begin);
        map.put("status", status);
        return orderMapper.getCountByMap(map);
    }
}
