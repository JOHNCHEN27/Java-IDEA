package com.lncanswer.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lncanswer.entitly.Category;

public interface CategoryService extends IService<Category> {

    /*
    根据id删除 并判断是否关联其他表
     */
    public void remove (Long id);
}
