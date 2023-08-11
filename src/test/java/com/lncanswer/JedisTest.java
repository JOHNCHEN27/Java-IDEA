package com.lncanswer;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

/*
使用Jedis操作Redis
 */
public class JedisTest {

    @Test
    public void testRedis(){
        //1、获取连接
        Jedis jedis = new Jedis("192.168.12.128",6379); //连接的主机和端口号

        //执行具体的操作
        jedis.auth("root");
        jedis.set("username","xiaoliu");

        //存取哈希结构的数据
        jedis.hset("shopping","amount","20");
        jedis.hset("shopping","msg","好吃的");

        Map<String,String> value = jedis.hgetAll("shopping");
        System.out.println(value);


        //关闭连接
        jedis.close();
    }
}
