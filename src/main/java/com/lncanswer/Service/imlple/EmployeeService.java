package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.Employee;
import com.lncanswer.mapper.EmployMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeService extends ServiceImpl<EmployMapper,Employee> implements com.lncanswer.Service.EmployeeService {
}
