package com.hwua.service;

import com.hwua.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface Userservice {
    public List<User> findAllUsers();
}
