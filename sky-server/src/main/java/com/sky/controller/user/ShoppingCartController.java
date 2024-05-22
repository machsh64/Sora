package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-22 22:52
 * @description:
 **/
@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端-购物车相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车信息：{}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取购物车列表")
    public Result<List<ShoppingCart>> getCartList() {

        List<ShoppingCart> list = shoppingCartService.getList();
        return Result.success(list);
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车中的一个")
    public Result deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean() {

        shoppingCartService.clean();
        return Result.success();
    }

}
