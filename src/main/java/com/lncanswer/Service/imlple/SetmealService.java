package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.dto.SetmealDto;
import com.lncanswer.entitly.Setmeal;
import com.lncanswer.entitly.SetmealDish;
import com.lncanswer.mapper.SetmealDishMapper;
import com.lncanswer.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements com.lncanswer.Service.SetmealService {

    @Autowired
    SetmealDishService setDishService;
    /*
    将套餐信息同时加入到套餐表和套餐关联表
     */
    @Override
    @Transactional
    public void insertSetmealAndDish(SetmealDto setmealDto) {
        //将基础信息添加到套餐表
        this.save(setmealDto);
        //将基础信息添加到套餐关联表
        List<SetmealDish> setmealDish =  setmealDto.getSetmealDishes();

        setmealDish.stream().map((item)->{

            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setDishService.saveBatch(setmealDish);

    }
}
