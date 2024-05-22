package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-15 21:07
 * @description:
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @return
     */
    @CacheEvict(cacheNames = "setmeal", key = "#setmealDTO.categoryId")
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {

        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("获取套餐分页")
    public Result<PageResult> getPage(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageResult page = setmealService.getPage(setmealPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 根据ids删除套餐
     *
     * @param ids
     * @return
     */
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    @DeleteMapping
    @ApiOperation("根据ids删除套餐")
    public Result deleteByIds(@RequestParam List<Long> ids) {

        setmealService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据id获取套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id获取套餐")
    public Result<SetmealVO> getById(@PathVariable("id") Long id) {

        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐
     *
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    @PutMapping
    @ApiOperation("更新套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO) {

        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 设置套餐的停售启售
     *
     * @return
     */
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    @PostMapping("/status/{status}")
    @ApiOperation("设置启售停售")
    public Result setStatus(@PathVariable("status") Integer status, Long id) {

        setmealService.setStatus(status, id);
        return Result.success();
    }

}




















