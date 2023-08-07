package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Dish;

public interface DishService extends IService<Dish> {

    public void insertDish(DishDto dishDto);
}
