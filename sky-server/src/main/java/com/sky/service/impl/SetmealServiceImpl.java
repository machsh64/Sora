package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-15 21:09
 * @description:
 **/
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        log.info("保存的套餐信息 setmealDTO: {}", setmealDTO);

        // 复制DTO信息到entity中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 获取菜品关联集合
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();

        // 保存套餐的基本信息
        setmealDishMapper.save(setmeal);

        // 保存套餐与菜品的关联
        // 给setmealDish设置sid
        setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.saveBatchCIdWithDishId(setmealDishList);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult getPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("查询套餐列表 setmealPageQueryDTO: {}", setmealPageQueryDTO);

        // 开启分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage = setmealDishMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(setmealVOPage.getTotal(), setmealVOPage.getResult());
    }

    /**
     * 根据ids删除套餐 以及 套餐关联表内的菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        log.info("删除的套餐ids: {}", ids);

        // 删除套餐表内的数据
        setmealDishMapper.deleteByIds(ids);
        // 删除套餐关联表内的菜品数据
        setmealDishMapper.deleteSetmealDishBySetmealId(ids);
    }

    /**
     * 根据id获取套餐数据
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {

        // 获取Setmeal套餐数据
        Setmeal setmeal = setmealDishMapper.getById(id);
        // 获取setmealDish关联菜品数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishesBySetmealId(id);
        // 将数据包装到VO里
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        log.info("修改套餐信息 setmealDTO: {}", setmealDTO);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 更新套餐基本信息
        setmealDishMapper.update(setmeal);

        // 删除旧的关联菜品
        setmealDishMapper.deleteSetmealDishBySetmealId(Collections.singletonList(setmeal.getId()));
        // 更新套餐关联菜品
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        // 给setmealDish设置sid
        setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.saveBatchCIdWithDishId(setmealDishList);
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    @Override
    public void setStatus(Integer status, Long id) {
        log.info("修改套餐状态 status: {}, id: {}", status, id);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);

        setmealDishMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealDishMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealDishMapper.getDishItemBySetmealId(id);
    }
}
















