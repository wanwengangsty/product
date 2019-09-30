package com.hwua.service.impl;

import com.hwua.entity.User;
import com.hwua.mapper.UserMapper;
import com.hwua.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserserviceImpl implements Userservice {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }
}
