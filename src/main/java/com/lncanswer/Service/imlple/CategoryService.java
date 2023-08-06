package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.ExceptionHandler.CustmoerException;
import com.lncanswer.entitly.Category;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.Setmeal;
import com.lncanswer.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements com.lncanswer.Service.CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public void remove(Long id) {
        //条件查询： 查找是否关联菜品id
        LambdaQueryWrapper<Dish> dishLam = new LambdaQueryWrapper<>();
        dishLam.eq(Dish::getCategoryId,id);
        Long count1 = dishService.count(dishLam);
        //当前分类是否关联了菜品，如果关联 则抛出业务异常 不能删除
        if (count1 >0){
            //抛出异常
             throw new CustmoerException("当前分类下关联了菜品，不能删除");
        }

        //当前分类是否关联了套餐，如果关联 则抛出业务异常，不能删除
        LambdaQueryWrapper<Setmeal> setLam = new LambdaQueryWrapper<>();
        setLam.eq(Setmeal::getCategoryId,id);
        Long count2 = setmealService.count(setLam);
        if (count2 > 0){
            //抛出异常
            throw new CustmoerException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        categoryMapper.deleteById(id);
        //super.removedById 也可以调用父类的方法进行删除

    }
}
