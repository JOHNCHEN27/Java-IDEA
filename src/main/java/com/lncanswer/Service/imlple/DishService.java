package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.Service.DishFlavorService;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements com.lncanswer.Service.DishService {
    @Autowired
    private DishFlavorService dishFlavor;

    /*
    同时保存菜品和菜品口味
     */
    @Override
    @Transactional //涉及多张表操作 需要开启事务
    public void insertDish(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        //菜品口味
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //批量保存
        dishFlavor.saveBatch(flavors);

    }
}
