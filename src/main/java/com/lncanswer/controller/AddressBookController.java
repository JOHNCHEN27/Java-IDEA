package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lncanswer.Service.imlple.AddressBookService;
import com.lncanswer.entitly.AddressBook;
import com.lncanswer.entitly.Result;
import com.lncanswer.utils.BaseContext;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /*
    新增收获地址
     */
    @PostMapping
    public Result<String> save (@RequestBody AddressBook addressBook, HttpSession httpSession){
        log.info("根据收货信息来添加收货地址：{}",addressBook);
        if(addressBook != null){
            addressBook.setUserId((Long) httpSession.getAttribute("user"));
            addressBookService.save(addressBook);
            return Result.success("添加收获地址成功");
        }

        return Result.error("添加收获地址失败");
    }

    /*
    查询收获地址列表
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> selectList(HttpSession session){
        log.info("查询列表：");
        LambdaQueryWrapper<AddressBook> lam = new LambdaQueryWrapper<>();
        Long id = (Long) session.getAttribute("user");
        lam.eq(AddressBook::getUserId,id);
        List<AddressBook> list = addressBookService.list(lam);
        if (list != null){
            return Result.success(list);
        }
        return Result.error("查询失败");
    }

    /*
    设置为默认地址
     */
    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        log.info("将当前用户的地址修改为默认地址");
        AddressBook addressBook1 = addressBookService.updateDefaultAddress(addressBook);
        if (addressBook1 != null){
            return Result.success(addressBook1);
        }
        return Result.error("查找失败！");
    }

    @GetMapping("/default")
    public Result<AddressBook> selectDefault(){
        log.info("查询默认地址信息");
        LambdaQueryWrapper<AddressBook> lam = new LambdaQueryWrapper<>();
        lam.eq(AddressBook::getIsDefault,1);
        lam.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        AddressBook addressBook =addressBookService.getOne(lam);
        if (addressBook != null){
            return Result.success(addressBook);
        }
        return Result.error("查询失败");
    }
}
