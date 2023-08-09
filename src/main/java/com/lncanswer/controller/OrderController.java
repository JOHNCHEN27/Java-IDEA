package com.lncanswer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lncanswer.Service.imlple.OrderService;
import com.lncanswer.entitly.Orders;
import com.lncanswer.entitly.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    /*
    查询订单分页
     */
    @GetMapping("/userPage")
    public Result<Page> selectPage(Integer page,Integer pageSize){
        log.info("查询分页：{},{}",page,pageSize);
        Page page1 = orderService.selectPage(page,pageSize);
        if (page1 != null)
        {
            return Result.success(page1);
        }
        return Result.error("查询分页失败");
    }

    /*
    提交订单
     */
    @PostMapping("/submit")
    public Result<String> submitOrder(@RequestBody Orders orders){
        log.info("提交订单：{}",orders);
        try {
            orderService.postOrder(orders);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("提交订单失败");
        }
        return Result.success("提交订单成功");
    }
}
