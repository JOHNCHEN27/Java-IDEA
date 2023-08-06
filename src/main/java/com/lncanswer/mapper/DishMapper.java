package com.lncanswer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lncanswer.entitly.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
