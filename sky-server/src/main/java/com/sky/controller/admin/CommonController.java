package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-14 21:37
 * @description: 通用接口
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestParam("file") MultipartFile multipartFile) {
        log.info("文件上传: {}", multipartFile);

        try {
            // 获取原始文件名
            String originalFilename = multipartFile.getOriginalFilename();
            // 截取原始文件名后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构建新文件名称
            String fileName = UUID.randomUUID() + extension;
            // 文件的请求路径
            String filePath = aliOssUtil.upload(multipartFile.getBytes(), fileName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
