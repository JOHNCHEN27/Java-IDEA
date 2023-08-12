package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Category;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.entitly.Result;
import com.lncanswer.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements com.lncanswer.Service.DishService {
    @Autowired
    private DishFlavorImpleService dishFlavor;
    @Autowired
    @Lazy //当两个类存在相应的bean依赖时会引发循环依赖问题，springboot默认不允许
    //Lazy注解可以延迟加载bean 可以解决单例循环依赖问题
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

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

    /*
    根据菜品id和售卖状态查询菜品
     */
    @Override
    public List<DishDto> selectByCategoryAndStatus(Long categoryId, int status) {

        List<DishDto> dishDtos = null;
        //动态构造key值
        String key ="dish_" +categoryId+"_"+status;

        //先从redis中获取缓存数据
        dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if(dishDtos != null){
            //如果redis中存在数据,直接返回,无需查询数据库
            return dishDtos;
        }
        //如果不存在 则需要查询数据库，
        LambdaQueryWrapper<Dish> lam = new LambdaQueryWrapper<>();
        lam.eq(categoryId!= null,Dish::getCategoryId,categoryId);
        lam.eq(Dish::getStatus,status);
        lam.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = this.list(lam);
         dishDtos = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Category category = categoryService.getById(item.getCategoryId());
            if (category!=null){
                dishDto.setCategoryName(category.getName());
            }
            //查询口味数据
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId,item.getId());

            List<DishFlavor>  dishFlavors = dishFlavor.list(wrapper);
            //将查询出来的口味数据封装到dishDto对象中的属性
            dishDto.setFlavors(dishFlavors);
            return dishDto;

        }).collect(Collectors.toList());

        //将查询到的菜品数据缓存到Redis中 设置缓存时间六十分钟
        redisTemplate.opsForValue().set(key,dishDtos,60, TimeUnit.MINUTES);

        return dishDtos;
    }
}
