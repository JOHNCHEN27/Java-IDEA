package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.lncanswer.ExceptionHandler.CustmoerException;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.entitly.Result;
import com.lncanswer.entitly.ShoppingCart;
import com.lncanswer.mapper.ShoppingCartMapper;
import com.lncanswer.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RelationSupport;
import java.util.List;
@Slf4j
@Service
public class ShoppingCartService extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements com.lncanswer.Service.ShoppingCartService {

    @Autowired
    DishFlavorImpleService dishService;
    /*
    根据购物车实体信息查询菜品口味
     */
    @Override
    public ShoppingCart  selectDishFlavor(ShoppingCart shoppingCart) {
        //先将实体信息保存到购物车中 保存之前将userid设置默认值
        log.info("userid： {}", BaseContext.getCurrentId());
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //shoppingCart.setDishId(shoppingCart.getSetmealId());

        //判断添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();


        LambdaQueryWrapper<ShoppingCart> lam = new LambdaQueryWrapper<>();
        lam.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        if (dishId != null) {
            //查询的是菜品
            lam.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是菜品
            lam.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或套餐是否在购物车中
        ShoppingCart cart = this.getOne(lam);

        if (cart != null) {
            //说明当前数据已经在购物车中，在原来的基础上加1
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            //根据当前的数量更新购物车
            this.updateById(cart);
        } else {
            //如果不存在，将其默认值设为1
            shoppingCart.setNumber(1);
            this.save(shoppingCart);
            cart = shoppingCart;
        }

        return cart;
    }}
        //根据菜品id值来查找口味信息并返回

        //注意：传进来的setmealId对应DishId
        //sql：select * from dish_flavor where dish_id = SetmealId
      //  List<DishFlavor> list = dishService.list(lam);
//        if(list != null){
//            return list;
//        }
//        throw  new CustmoerException("查询错误");
//    }

