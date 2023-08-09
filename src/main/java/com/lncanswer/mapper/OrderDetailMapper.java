package com.lncanswer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lncanswer.entitly.OrderDetail;
import com.lncanswer.entitly.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
