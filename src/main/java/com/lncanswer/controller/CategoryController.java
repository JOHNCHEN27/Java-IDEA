package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lncanswer.Service.imlple.CategoryService;
import com.lncanswer.entitly.Category;
import com.lncanswer.entitly.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /*
    分页请求
     */
    @GetMapping("/page")
    public Result<Page> selectPage(Integer page, Integer pageSize){
        log.info("分页：{},{}",page,pageSize);
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Category> lam = new LambdaQueryWrapper<>();
        lam.orderByAsc(Category::getSort);
        categoryService.page(page1,lam);
        return Result.success(page1);
    }

    /*
    新增菜品分类
     */
    @PostMapping
    public Result<String> insert(@RequestBody Category category){
        log.info("新增菜品分类：{}",category);
        if(category != null) {
            categoryService.save(category);
            return Result.success("新增菜品分类成功");
        }
        return Result.error("新增菜品分类失败");
    }

    /*
    修改菜品信息
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category){
        log.info("修改菜品信息:{}",category);
        if (category!= null){
            categoryService.updateById(category);
            return Result.success("修改菜品信息成功");
        }
        return Result.error("修改菜品信息失败");
    }

    /*
    删除菜品信息
     */
    @DeleteMapping
    public Result<String> deleteById(Long ids){
        log.info("根据id删除：{}",ids);
        if(ids != null) {
            //categoryService.removeById(ids);
            categoryService.remove(ids);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");

    }

    /*
    根据type属性查询 回显菜品分类到下拉框
     */
    @GetMapping("/list")
    public Result <List<Category>> selectByType( Category category){
        log.info("根据type查找：{}",category.getType());
        LambdaQueryWrapper<Category> lam = new LambdaQueryWrapper<>();
        lam.eq(category.getType()!=null,Category::getType,category.getType());
        List<Category> list = categoryService.list(lam);

        return Result.success(list);
    }

}
