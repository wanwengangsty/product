package com.hwua.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisUtil {
    private static JedisPool jedisPool;
    static{
        //创建一个jedis连接池的配置类对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        InputStream is = JedisUtil.class.getClassLoader().getResourceAsStream("redisConfig.properties");
        Properties props = new Properties();
        try {
            props.load(is);
            poolConfig.setMaxIdle(Integer.parseInt(props.getProperty("jedis.maxIdle")));
            poolConfig.setMaxTotal(Integer.parseInt(props.getProperty("jedis.maxTotal")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        jedisPool = new JedisPool(poolConfig,props.getProperty("jedis.host"),Integer.parseInt(props.getProperty("jedis.port")));
    }

    //获得一个jedis客户端的连接对象
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    //关闭客户端连接
    public static void close(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    //
    // 客户端发送一次ajax请求,查询所有的新闻,在页面上显示所有新闻内容
    // 从数据库查询所有的新闻,要求把集合转换成json格式的字符串 放到redis中, news:json格式字符串
    // 下次查询直接从redis中读取json格式的数据,在页面上进行显示

}
