package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-15 21:09
 * @description:
 **/
public interface SetmealService {

    /**
     * 保存套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult getPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ids删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id获取套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 更新套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据id设置套餐的启停售
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
