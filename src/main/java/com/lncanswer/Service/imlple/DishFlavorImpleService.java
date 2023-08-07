package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.entitly.DishFlavor;
import com.lncanswer.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorImpleService extends ServiceImpl<DishFlavorMapper, DishFlavor> implements com.lncanswer.Service.DishFlavorService {
}
