package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.entitly.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart selectDishFlavor(ShoppingCart shoppingCart);
}
