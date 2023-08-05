package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lncanswer.Service.EmployeeService;
import com.lncanswer.entitly.Employee;
import com.lncanswer.entitly.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lam = new LambdaQueryWrapper<>();
        lam.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lam);

        //如果没有查询到返回错误信息
        if(emp == null){
            return Result.error("登陆失败");
        }

        //如果密码不一致则返回错误信息
        if(!emp.getPassword().equals(password)){
            return Result.error("登录失败");
        }

        //查看员工状态 ，如果状态被禁用，则登录失败
        if(emp.getStatus() == 0){
            return Result.error("账号被禁用");
        }

        //登录成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return  Result.success(emp);
    }
    /*
    员工退出 @param request  @return
     */
    @PostMapping("logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /*
    新增员工
     */
    @PostMapping
    public Result<String> save (HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee);

        //设置员工的初始密码，创建时间、更新时间、创建人id、修改人id
        //设置密码的时候将密码进行md5加密 保证安全
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取当前用户的id 将存放创建人id、修改人id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    /*
    查询分页
     */
    @GetMapping("/page")
    public Result<Page> selectPage(Integer page, Integer pageSize,String name){
        log.info("{},{},{}",page,pageSize,name);
        //创建分页
        Page page1 = new Page(page,pageSize); //page为当前页，pageSize表示每页查询多少条

        //设置条件查询， 利用lambda表达式
        LambdaQueryWrapper<Employee> lam = new LambdaQueryWrapper<>();
        //判断 name是否为空， 为空不执行条件判断，
        lam.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lam.orderByDesc(Employee::getUpdateTime); //根据更新时间降序排列
        employeeService.page(page1,lam); //查询之后 会将数据自动封装到原来的page1中不需要再另外接受
        return Result.success( page1);
    }

    /*
    禁用员工状态
     */
    @PutMapping
    public Result<String> empStatus(HttpServletRequest request,@RequestBody Employee employee){
        log.info("更改员工状态：{}",employee);
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        return Result.success("修改员工成功");

    }

    /*
    根据id查询用户信息并返回对象
     */
    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable Long    id){
        log.info("根据id查询：{}",id);
       Employee employee =  employeeService.getById(id);
       if (employee != null){
           return Result.success(employee);
       }

       return Result.error("没有查询到对应的员工信息");


    }

}
