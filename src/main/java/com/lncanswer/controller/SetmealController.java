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
        lam.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(page1,lam);

        List<Setmeal> setmealList = page1.getRecords();
        //利用page1封装的数据查询菜品 将此集合转换类型 最后封装到page2数据中
        List<SetmealDto> setmealDtos = setmealList.stream().map((item) ->{
            //创建setmealDto对象 利用Bean工具类把每个item的属性赋值给当前对象
            //通过后面获取的菜品名称最后给这个对象赋值，然后返回当前对象
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);
            //根据集合中封装的数据来查询菜品id 获取菜品
            Long Id =  item.getCategoryId();
            Category category = categoryService.getById(Id);
            //如果获取的菜品不为空则进行赋值操作
            if (category!=null){
                setmealDto.setCategoryName(category.getName());
            }
           return setmealDto;
        }).collect(Collectors.toList());

        //将封装的集合数据封装到page2中 返回page2分页对象
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

    /*
    根据ids删除 批量删除
     */
    @DeleteMapping
    public Result<String> deleteByIds(Long [] ids){
        log.info("根据ids数组删除：{}",ids);
        if(ids != null){
            setmealService.deleteByIds(ids);
            return Result.success("删除成功");
        }

        return Result.error("删除失败");
    }

    /*
    设置停售状态
     */
    @PostMapping("/status/{path}")
    public Result<String> updateStatus(@PathVariable int path, Long [] ids){
        log.info("根据id修改套餐状态：{}",ids);
        if(ids  != null){
            setmealService.updateStatus(path,ids);
            return Result.success("操作成功");
        }
        return Result.error("操作失败");

    }

}
