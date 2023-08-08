package com.lncanswer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lncanswer.entitly.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
