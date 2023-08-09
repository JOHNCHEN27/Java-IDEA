package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.entitly.Orders;

public interface OrderService extends IService<Orders> {
    Page selectPage(Integer page, Integer pageSize);

    void postOrder(Orders orders);
}
