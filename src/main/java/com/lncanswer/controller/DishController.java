package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lncanswer.Service.imlple.CategoryService;
import com.lncanswer.Service.imlple.DishService;
import com.lncanswer.dto.DishDto;
import com.lncanswer.entitly.Category;
import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/*
菜品管理
 */

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    菜品分页查询
     */
    @GetMapping("/page")
    public Result<Page> selectPage(Integer page,Integer pageSize,String name){
        Page page1 = new Page<>(page,pageSize);
        //构造DishDto 分页
        Page<DishDto> dishDtoPage= new Page<>();
        BeanUtils.copyProperties(page1,dishDtoPage,"records");

        LambdaQueryWrapper<Dish> lam = new LambdaQueryWrapper<>();
        lam.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        lam.orderByDesc(Dish::getUpdateTime);
        dishService.page(page1,lam);

        List<Dish> records = page1.getRecords();
        List<DishDto> list =  records.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId(); //分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    /*
    新增菜品
     */
    @PostMapping
    public Result<String> insertDish(@RequestBody DishDto dishDto){
        log.info("dishDto:{}",dishDto);
        if (dishDto != null) {
            dishService.insertDish(dishDto);

            //清理某个菜品下Redis缓存数据
            String key = "dish_" + dishDto.getCategoryId() + "_1";
            redisTemplate.delete(key);

            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /*
    修改菜品 先进行数据回显 再修改菜品
     */
    @GetMapping("/{id}")
    public Result<DishDto> selectDishById(@PathVariable Long id){
        log.info("根据id查询相应的菜品信息以及菜品口味:{}",id);
        DishDto dishDto = dishService.selectDishWithFlavor(id);
        return Result.success(dishDto);
    }

    @PutMapping
    public Result<String>  updateDish(@RequestBody DishDto dishDto){
        log.info("根据传进来的信息：{}",dishDto);
        if(dishDto != null) {
            dishService.updateDishAndFlavor(dishDto);

            //修改之后清理Redis中查询菜品所有的缓存数据
           // Set keys = redisTemplate.keys("dish_*");
            //redisTemplate.delete(keys);

            //清理某个分类下的c菜品缓存数据
            String key ="dish_" +dishDto.getCategoryId()+"_" +dishDto.getStatus();
            redisTemplate.delete(key);
            return Result.success("更改成功");
        }
        return Result.error("更改失败");
    }

    /*
    查询dish表中的套餐里包含的菜品
     */
//    @GetMapping("/list")
//    public Result<List<Dish>> selectList(Long categoryId,int status){
//        log.info("根据id查询菜品：{},{}",categoryId,status);
//
//        List<Dish> dishList = dishService.selectByCategoryAndStatus(categoryId,status);
//        if (dishList != null) {
//            return Result.success(dishList);
//        }
//        return Result.error("查询失败");
//    }

    @GetMapping("/list")
    public Result<List<DishDto>> selectList(Long categoryId,int status){
        log.info("根据id查询菜品：{},{}",categoryId,status);

        List<DishDto> dishList = dishService.selectByCategoryAndStatus(categoryId,status);
        if (dishList != null) {
            return Result.success(dishList);
        }
        return Result.error("查询失败");
    }

}
