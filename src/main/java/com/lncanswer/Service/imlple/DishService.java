package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.Dish;
import com.lncanswer.mapper.DishMapper;
import org.springframework.stereotype.Service;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements com.lncanswer.Service.DishService {
}
