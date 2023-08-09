package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.Orders;
import com.lncanswer.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetail extends ServiceImpl<OrderDetailMapper, com.lncanswer.entitly.OrderDetail> implements com.lncanswer.Service.OrderDetail {
}
