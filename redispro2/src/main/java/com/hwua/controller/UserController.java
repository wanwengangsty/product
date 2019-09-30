package com.hwua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwua.entity.User;
import com.hwua.service.Userservice;
import com.hwua.util.JedisUtil;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private Userservice userservice;

    @RequestMapping("/findAllUsers")
    @ResponseBody
    public String findAllUsers() throws JsonProcessingException {
        Jedis jedis = JedisUtil.getJedis();
        String news = jedis.get("news");
        if (news == null){
            List<User> userList = userservice.findAllUsers();
            ObjectMapper objectMapper = new ObjectMapper();
            news = objectMapper.writeValueAsString(userList);
            jedis.set("news",news );
            System.out.println("从数据库中读取数据!");
        }else{
            news = jedis.get("news");
            System.out.println("从redis中读取数据!");
        }
        jedis.close();
        return news;
    }
}
