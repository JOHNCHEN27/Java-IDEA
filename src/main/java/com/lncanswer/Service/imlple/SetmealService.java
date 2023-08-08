package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.ExceptionHandler.CustmoerException;
import com.lncanswer.dto.SetmealDto;
import com.lncanswer.entitly.Setmeal;
import com.lncanswer.entitly.SetmealDish;
import com.lncanswer.mapper.SetmealDishMapper;
import com.lncanswer.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements com.lncanswer.Service.SetmealService {

    @Autowired
    SetmealDishService setDishService;

    @Autowired
    SetmealMapper setmealMapper;
    /*
    将套餐信息同时加入到套餐表和套餐关联表
     */
    @Override
    @Transactional
    public void insertSetmealAndDish(SetmealDto setmealDto) {
        //将基础信息添加到套餐表
        this.save(setmealDto);
        //将基础信息添加到套餐关联表
        List<SetmealDish> setmealDish =  setmealDto.getSetmealDishes();

        setmealDish.stream().map((item)->{

            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setDishService.saveBatch(setmealDish);

    }

    /*
    根据id数组删除相应的的套餐数据
     */
    @Override
    @Transactional  //开启事务管理， 防止删除错误
    public void deleteByIds(Long[] ids) {
        //删除之前首先判断套餐的状态 如果在售卖就不能删除否则可以删除
        LambdaQueryWrapper<Setmeal> lam = new LambdaQueryWrapper<>();
        lam.in(Setmeal::getId,ids);
        lam.eq(Setmeal::getStatus,1);
        int count = (int) this.count(lam);  //查询出来看是否有记录

        if(count > 0){
            //有套餐状态在售卖中，不能删除,抛出一个业务异常
            throw new CustmoerException("套餐正在售卖中，不能删除！！！");
        }

        //首先删除套餐表中的信息
        this.deleteByIds(ids);

        //删除关联套餐表中的信息
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //mybatisPlus通过表主键来删除项目，而这里我们需要用到的不是根据主键删除
        //需要自己加上条件判断
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setDishService.remove(lambdaQueryWrapper);

    }

    /*
    根据id修改相应的套餐状态
     */
    @Override
    public void updateStatus(int path , Long [] ids) {
        //根据ids拿到相应的套餐数据
        List<Long> Ids = List.of(ids);
        List<Setmeal> list = this.listByIds(Ids);

         list.stream().map((item) ->{
            //int status = item.getStatus();
            if (path == 1){
                item.setStatus(1);
            }else{
                item.setStatus(0);
            }
             setmealMapper.updateById(item);
             return item;
         }).collect(Collectors.toList());
    }
}
