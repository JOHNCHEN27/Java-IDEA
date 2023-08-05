package com.lncanswer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lncanswer.entitly.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployMapper extends BaseMapper<Employee> {
}
