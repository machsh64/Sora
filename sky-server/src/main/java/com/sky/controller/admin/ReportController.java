package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-26 21:05
 * @description:
 **/
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> getTurnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverStatistics);
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> getUserReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
        return Result.success(userReportVO);
    }


    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> getOrderReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        OrderReportVO orderReportVO = reportService.getOrderReport(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 销量前十
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("销量前十")
    public Result<SalesTop10ReportVO> getTop10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        SalesTop10ReportVO salesTop10ReportVO = reportService.getTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出表格数据
     */
    @GetMapping("/export")
    @ApiOperation("导出表格数据")
    public void export(HttpServletResponse response) {

        // 1 查询数据库, 获取营业数据 - 查询最近30天的营业数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDateTime beginTime = LocalDateTime.of(dateBegin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(dateEnd, LocalTime.MAX);
        // 查询概览数据
        BusinessDataVO businessData = workspaceService.getBusinessData(beginTime, endTime);

        // 2 通过POI将数据写入xlsx文件中
        InputStream inputStream = null;
        XSSFWorkbook excel = null;
        OutputStream outputStream = null;

        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
            assert inputStream != null;
            excel = new XSSFWorkbook(inputStream);
            // 获取表格的sheet页
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            // 填充时间
            sheet1.getRow(1).getCell(1).setCellValue("时间: " + dateBegin + " 至 " + dateEnd);

            // 获得第4行
            XSSFRow row3 = sheet1.getRow(3);
            // 填充营业额
            row3.getCell(2).setCellValue(businessData.getTurnover());
            // 填充订单完成率
            row3.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            // 填充新增用户数
            row3.getCell(6).setCellValue(businessData.getNewUsers());

            // 获得第5行
            XSSFRow row4 = sheet1.getRow(4);
            // 填充有效订单数
            row4.getCell(2).setCellValue(businessData.getValidOrderCount());
            // 填充平均客单价
            row4.getCell(4).setCellValue(businessData.getUnitPrice());

            // 填充明细数据
            for(int i = 0; i < 30; i++){
                LocalDate date = dateBegin.plusDays(i);
                LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

                // 查询某一天的营业数据
                BusinessDataVO businessDateDay = workspaceService.getBusinessData(begin, end);

                // 获得某一行
                XSSFRow row = sheet1.getRow(i + 7);
                // 设置日期
                row.getCell(1).setCellValue(date.toString());
                // 设置营业额
                row.getCell(2).setCellValue(businessDateDay.getTurnover());
                // 设置有效订单数
                row.getCell(3).setCellValue(businessDateDay.getValidOrderCount());
                // 设置订单完成率
                row.getCell(4).setCellValue(businessDateDay.getOrderCompletionRate());
                // 设置平均客单数
                row.getCell(5).setCellValue(businessDateDay.getUnitPrice());
                // 设置新增用户数
                row.getCell(6).setCellValue(businessDateDay.getNewUsers());
            }

            // 3 通过输出流将excel文件下载到客户端浏览器
           outputStream  = response.getOutputStream();
           excel.write(outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (excel != null) {
                    excel.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
