package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-14 22:55
 * @description:
 **/
public interface DishService {

    /**
     * 保存菜品信息，包括口味信息
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult getDishPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品的批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    DishVO getDishWithFlavorById(Long id);

    /**
     * 更新菜品信息和口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
