package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.dto.SetmealDto;
import com.lncanswer.entitly.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    void insertSetmealAndDish(SetmealDto setmealDto);
}
