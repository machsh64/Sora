package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-14 22:55
 * @description:
 **/
public interface DishService {

    /**
     * 保存菜品信息，包括口味信息
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}