package com.lncanswer.Service.imlple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lncanswer.Service.UserService;
import com.lncanswer.entitly.User;
import com.lncanswer.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple extends ServiceImpl<UserMapper, User> implements UserService {
}
