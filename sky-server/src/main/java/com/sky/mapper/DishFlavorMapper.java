package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-14 23:07
 * @description:
 **/
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavorList
     */
    void insertBatch(List<DishFlavor> flavorList);
}
