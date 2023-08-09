package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.lncanswer.Service.imlple.ShoppingCartService;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.entitly.Result;
import com.lncanswer.entitly.ShoppingCart;
import com.lncanswer.utils.BaseContext;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService cartService;

    /*
    查询购物车分页
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> shoppingCartList(){
        log.info("查询购物车分页列表");
        LambdaQueryWrapper <ShoppingCart> lam = new LambdaQueryWrapper<>();
//        Long id = Long.valueOf(BaseContext.getCurrentId().toString().substring(11));
        lam.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        List<ShoppingCart> list = cartService.list(lam);
        if (list == null)
        {
            return Result.error("查询失败");
        }
        return Result.success(list);
    }

    /*
    添加菜品查询菜品的口味信息
     */
    @PostMapping("/add")
    public Result<ShoppingCart> selectDishFlavor(@RequestBody ShoppingCart shoppingCart){
        log.info("根据实体信息查询：{}",shoppingCart);
        ShoppingCart shoppingCart1  = cartService.selectDishFlavor(shoppingCart);
        if (shoppingCart1 != null)
        {
            return Result.success(shoppingCart1);
        }
        return Result.error("查询失败");
    }

    /*
    清空购物车
     */
    @DeleteMapping("/clean")
    public Result<String> delete(HttpSession session){
        log.info("清空购物车");
        LambdaQueryWrapper<ShoppingCart> lam = new LambdaQueryWrapper<>();
        //Long id = Long.valueOf(BaseContext.getCurrentId().toString().substring(11));
        lam.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        cartService.remove(lam);
        return Result.success("清空购物车成功");
    }
}
