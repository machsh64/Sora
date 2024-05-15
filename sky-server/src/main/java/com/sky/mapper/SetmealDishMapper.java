package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    //@Select("select id from setmeal where id in (select setmeal_id from setmeal_dish where dish_id in (#{dishIds}))")
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 新增套餐信息
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);

    /**
     * 保存菜品信息与套餐信息关联
     * @param setmealDishList
     */
    void saveBatchCIdWithDishId(List<SetmealDish> setmealDishList);

    /**
     * 套餐分页信息查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ids删除套餐信息
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据Setmealids删除套餐与菜品关联信息
     * @param setmealIds
     */
    void deleteSetmealDishBySetmealId(List<Long> setmealIds);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据setmealId获取setmealDish关联菜品数据
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getSetmealDishesBySetmealId(Long setmealId);

    /**
     * 更新套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
