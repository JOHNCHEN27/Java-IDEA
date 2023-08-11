package com.lncanswer;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest

public class ReggieApplicationTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    操作String类型的数据
     */
    @Test
    public void testString(){
        //调用opsForValue方法返回的是 String 类型的键值对
        redisTemplate.opsForValue().set("city","北京");

        //设置超时时间
        redisTemplate.opsForValue().set("key1","10l",10, TimeUnit.MINUTES);

        //如果key存在时不设置key  如果key1键值存在 则不会创建， 不存在则将创建
        Boolean b  = redisTemplate.opsForValue().setIfAbsent("key1","name1");
        System.out.println(b);
    }

    /**
     * 操作Hash数据类型
     */
    @Test
    public void testHash(){
        //调用opsForHash返回的是Hash操作类型
        HashOperations hashOperations = redisTemplate.opsForHash();

        //存值 key -field value  -- 键值-属性兼值-属性值
        hashOperations.put("002","name","zs");
        hashOperations.put("002","age","22");
        hashOperations.put("002","addr","cs");
        hashOperations.put("002","sex","男");

        //取值
        String name = (String) hashOperations.get("002","name");
        System.out.println(name);

        //获取该哈希结构中的所有字段（属性键值）
        Set keys = hashOperations.keys("002");
        for(Object key : keys){
            System.out.println(key);
        }

        //获取hash结构中的所有值
        List values = hashOperations.values("002");
        for (Object val : values) {
            System.out.println(val);
        }
    }

    /**
     * 操作list类型的数据
     */
    @Test
    public void testList(){
        //利用redis对象来获取List对象 进行数据操作
        ListOperations listOperations = redisTemplate.opsForList();

        //存值
        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","b","c","d");

        //取值  0表示第一个数， -1表示最后一个数 这个结构与栈相似，先进后出
        //队列的栈的属性都有 要看具体怎么操作列表
        List<String> list = listOperations.range("mylist",0,-1);
        for(String val : list){
            System.out.println(val);
        }

        //获取队列的长度llen
        Long size = listOperations.size("mylist");
    }

    /**
     *操作set数据类型
     */
    @Test
    public void testSet(){
        //获取set对象
       SetOperations setOperations = redisTemplate.opsForSet();
       //存值
        setOperations.add("myset","a","b","c","d");

        //取值
        Set<String> myset = setOperations.members("myset");
        for(String o : myset){
            System.out.println(o);
        }

        //删除成员
        setOperations.remove("myset","a","b");
    }

    /**
     * Zset数据类型
     */
    @Test
    public void testZset(){
        //获取Zset对象
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //存值
        zSetOperations.add("myzset","a",10);
        zSetOperations.add("myzset","b",20);

        //取值
        Set<String> zs = zSetOperations.range("myzset",0,-1);
        for(String val : zs){
            System.out.println(val);
        }

        //修改分数
        zSetOperations.incrementScore("myzset","a",20);

        //删除成员
        zSetOperations.remove("b");
    }

    /**
     * 通用操作
     */
    @Test
    public void testCommon(){
        //获取Redis中所有的keys
        Set<String> keys = redisTemplate.keys("*");
        for(String val : keys){
            System.out.println(val);
        }

        //判断某个key是否存值
        Boolean bool = redisTemplate.hasKey("itcase");

        //获取某个key的类型
        DataType val = redisTemplate.type("itcast");
        System.out.println(val.name());

        //删除某个key
        redisTemplate.delete("myzset");
    }


}
