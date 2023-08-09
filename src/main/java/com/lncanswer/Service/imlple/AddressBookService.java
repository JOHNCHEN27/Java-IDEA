package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.AddressBook;
import com.lncanswer.mapper.AddressBookMapper;
import com.lncanswer.utils.BaseContext;
import jakarta.el.LambdaExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService extends ServiceImpl<AddressBookMapper, AddressBook> implements com.lncanswer.Service.AddressBookService {
    /*
    设置为默认地址
     */
    @Autowired
    AddressBookMapper addressBookMapper;
    @Override
    public AddressBook updateDefaultAddress(AddressBook addressBook) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getIsDefault,1);
        addressBook.setIsDefault(0);
        addressBookMapper.update(addressBook,wrapper);

        addressBook.setIsDefault(1);
        this.updateById(addressBook);
        return addressBook;
    }
}
