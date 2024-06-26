package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-14 22:51
 * @description:
 **/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);

        dishService.saveWithFlavor(dishDTO);

        // 清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品: {}", dishPageQueryDTO);

        PageResult pageResult = dishService.getDishPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("根据id批量删除菜品")
    public Result deleteByIds(@RequestParam("ids") List<Long> ids) {
        log.info("根据id批量删除菜品: {}", ids);

        // 将所有的菜品缓存数据清理掉, 所有dish_开头的key
        cleanCache("dish_*");

        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品: {}", id);

        DishVO dishVO = dishService.getDishWithFlavorById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息: {}", dishDTO);

        dishService.updateWithFlavor(dishDTO);
        // 将所有的菜品缓存数据清理掉, 所有dish_开头的key
        cleanCache("dish_*");

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类categoryId查询菜品")
    public Result<List<DishVO>> getListByCategoryId(@RequestParam("categoryId") Long categoryId) {
        log.info("根据分类categoryId查询菜品: {}", categoryId);

        List<DishVO> dishVOList = dishService.getListByCategoryId(categoryId);
        return Result.success(dishVOList);
    }

    /**
     * 设置菜品的停售启售
     *
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("设置启售停售")
    public Result setStatus(@PathVariable("status") Integer status, Long id) {

        dishService.setStatus(status, id);
        // 将所有的菜品缓存数据清理掉, 所有dish_开头的key
        cleanCache("dish_*");

        return Result.success();
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
