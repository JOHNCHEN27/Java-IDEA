package com.lncanswer.utils;

/*
基于Th'readLocal封装的工具类，用于保存和获取当前登录用户id
 浏览器每次发送请求都是一次新的线程，一次线程所涉及到的所有的方法、类都对应这个线程
 ThreadLocal 具有线程隔离效果，每次线程都不同，不用担心数据的混淆
 */
public class BaseContext {
    //泛型为长整型 因为我们要存取的id类型为long
    private static  ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public  static Long  getCurrentId(){
        return threadLocal.get();
    }

}
