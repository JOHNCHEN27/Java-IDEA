package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.dto.SetmealDto;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void insertSetmealAndDish(SetmealDto setmealDto);

    void deleteByIds(Long[] ids);

    void updateStatus(int path ,Long [] ids);

    List<Dish> selecByCategoryAndStatus(Long categoryId, int status);
}
