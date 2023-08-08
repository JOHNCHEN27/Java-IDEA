package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.AddressBook;
import com.lncanswer.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService extends ServiceImpl<AddressBookMapper, AddressBook> implements com.lncanswer.Service.AddressBookService {
}
