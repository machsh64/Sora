package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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
}
