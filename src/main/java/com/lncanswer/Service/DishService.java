package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    public void insertDish(DishDto dishDto);

    Dish selectDishWithFlavor(Long id);

    void updateDishAndFlavor(DishDto dishDto);

    List<DishDto> selectByCategoryAndStatus(Long categoryId, int status);
}
