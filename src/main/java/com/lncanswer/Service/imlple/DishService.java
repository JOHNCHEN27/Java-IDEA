package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements com.lncanswer.Service.DishService {
    @Autowired
    private DishFlavorImpleService dishFlavor;


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

    /*
    根据id查询相应的菜品信息以及口味信息 用户数据回显
     */
    @Override
    public DishDto selectDishWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();

        //对象拷贝 将dish拷贝给dishDto
        BeanUtils.copyProperties(dish,dishDto);

        //查询菜品的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null,DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavor.list(queryWrapper);

        dishDto.setFlavors(dishFlavorList);
        return dishDto;

    }

    /*
    修改菜品的信息和口味
     */
    @Override
    @Transactional
    public void updateDishAndFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> lam = new LambdaQueryWrapper<>();
        lam.eq(DishFlavor::getDishId,dishDto.getId());

        //清理原先数据
        dishFlavor.remove(lam);

        List<DishFlavor> flavors = dishDto.getFlavors();

        //封装id flavors集合中只封装了name、value
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存集合
        dishFlavor.saveBatch(flavors);

    }
}
