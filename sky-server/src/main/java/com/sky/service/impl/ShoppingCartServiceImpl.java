package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-22 23:02
 * @description:
 **/
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        // 判断当前加入到购物车的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已经存在了, 商品数量加一
        if (list != null && list.size() > 0) {
            ShoppingCart shop = list.get(0);
            shop.setNumber(shop.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shop);
        } else {
            // 如果不存在, 则进行添加一条购物车数据
            // 判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if (dishId != null) {
                // 本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);

                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 本次添加到购物车的是套餐
                Setmeal setmeal = setmealDishMapper.getById(setmealId);

                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.addToCart(shoppingCart);

        }
    }

    /**
     * 查询购物车列表
     *
     * @return
     */
    @Override
    public List<ShoppingCart> getList() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .id(userId)
                .build();

        List<ShoppingCart> carts = shoppingCartMapper.list(shoppingCart);
        return carts;
    }

    /**
     * 删除购物车中的商品一次
     * @param shoppingCartDTO
     */
    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {

        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        // 查询购物车中的菜品数量
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        Integer number = list.get(0).getNumber();
        shoppingCart.setId(list.get(0).getId());
        // 判断删除还是数量减一
        if (number == 1) {
            shoppingCartMapper.remove(shoppingCart);
        } else {
            shoppingCart.setNumber(number - 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }
    }

    /**
     * 删除购物车中的商品全部
     */
    @Override
    public void clean() {

        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }
}
