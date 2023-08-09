package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.entitly.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    AddressBook updateDefaultAddress(AddressBook addressBook);
}
