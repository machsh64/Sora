package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-26 21:10
 * @description:
 **/
public interface ReportService {

    /**
     * 获取营业额统计
     * @param begin
     * @param end
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderReport(LocalDate begin, LocalDate end);

    /**
     * 获取销售排名 10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
