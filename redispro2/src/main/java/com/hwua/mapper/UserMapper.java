package com.hwua.mapper;

import com.hwua.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select id,name,pwd,email from users ")
    public List<User> findAllUsers();

}
