package com.lncanswer.filter;

import com.alibaba.fastjson.JSON;
import com.lncanswer.entitly.Result;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*") //拦截所有
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //将Servlet请求转换成Http对象请求
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //获取url路径 并且判断是否包含登录、退出、静态资源路径
        String url = req.getRequestURI();
        log.info("查看本次请求的路径：{}",url);
        //将不需要拦截的路径 存放到一个字符串数组中 并进行判断放行
        String [] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/",
                "/front/"
        };
        //判断请求路径中是否包含urls数组里需要放行的路径
        for (int i = 0; i < urls.length; i++) {
           // log.info("urls[i]:{}",urls[i]);
            if(url.contains(urls[i])){
                log.info("urls[i]: {}",urls[i],"  本次请求不需要处理");
                chain.doFilter(request,response);
                return;
            }
        }

        //判断登录状态，如果已登录直接放行
        if(req.getSession().getAttribute("employee") != null){
            log.info("员工已经登录：{}",req.getSession().getAttribute("employee"));
            chain.doFilter(request,response);
            return;
        }else {
            log.info("员工未登录 ！！！");
            res.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            return;
        }




    }
}
