package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lncanswer.Service.imlple.CategoryService;
import com.lncanswer.Service.imlple.SetmealService;
import com.lncanswer.dto.SetmealDto;
import com.lncanswer.entitly.Category;
import com.lncanswer.entitly.Result;
import com.lncanswer.entitly.Setmeal;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    /*
    分页查询
     */
    @GetMapping("/page")
    public Result<Page> selectPage(Integer page,Integer pageSize, String name){
        log.info("根据分页信息和姓名查询:{},{},{}",page,pageSize,name);
       Page page1 = new Page(page,pageSize);
        Page<SetmealDto> page2 = new Page<>();

        BeanUtils.copyProperties(page1,page2,"records");

        //分页查询数据将查询出来的数据封装到page1
        LambdaQueryWrapper<Setmeal> lam = new LambdaQueryWrapper<>();
        lam.like(name!= null,Setmeal::getName,name);
        setmealService.page(page1,lam);

        List<Setmeal> setmealList = page1.getRecords();

        List<SetmealDto> setmealDtos = setmealList.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

           Long Id =  item.getCategoryId();
           Category category = categoryService.getById(Id);

           setmealDto.setCategoryName(category.getName());
           return setmealDto;
        }).collect(Collectors.toList());

        page2.setRecords(setmealDtos);
        return Result.success(page2);
    }

    /*
    新增菜单
     */
    @PostMapping
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("插入新的套餐：{}",setmealDto);
        if (setmealDto != null) {
            setmealService.insertSetmealAndDish(setmealDto);
            return Result.success("添加成功");
        }

        return Result.error("添加失败");
    }

}
