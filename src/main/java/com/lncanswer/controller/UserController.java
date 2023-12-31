package com.lncanswer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lncanswer.Service.UserService;
import com.lncanswer.Service.imlple.UserServiceImple;
import com.lncanswer.entitly.Result;
import com.lncanswer.entitly.User;
import com.lncanswer.utils.SMSUtils;
import com.lncanswer.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    发送手机短信
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        log.info("查询用户的手机号：{}",user);
        //获取用户的手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机的四位数验证码,调用工具类
            String code  = ValidateCodeUtils.generateValidateCode(4).toString();
                log.info("code ={}",code);

            //调用阿里云提供的短信服务API完成发送短信
           // SMSUtils.sendMessage("lncanswer","SMS_284090456","phone","code");

            //将生成的验证码保存到session中
           // httpSession.setAttribute(phone,code);

            //将生成的验证码保存到Redis中，并且设置验证码为2分钟
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.success("手机验证码短信送成功");
        }
        return Result.error("手机验证码短信发送失败");
    }

    /*
    登录请求 移动端用户登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map user,HttpSession session){
        log.info(user.toString());
        //获取用户的手机号和验证码
        String phone  = user.get("phone").toString();
        String code = user.get("code").toString();
        //从session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);

        //从Redis中获取缓存的验证码
        Object codeInSession =  redisTemplate.opsForValue().get(phone);

        //进行验证码的对比
        if(codeInSession != null && codeInSession.equals(code)){
            //能够对比成功，则登录成功
            //如果用户登录成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);

            //判断当前用户是否为新用户，如果是新用户则自动完成注册
            LambdaQueryWrapper<User> lam =new LambdaQueryWrapper<>();
            lam.eq(User::getPhone,phone);
            User user1 = userService.getOne(lam);
            if (user1 == null){
                //新用户 为其设置手机号
                user1 = new User();
                user1.setPhone(phone);
                user1.setStatus(1);
                userService.save(user1);
            }
            log.info("user1Id:{}",user1.getId());
            session.setAttribute("user",user1.getId());

            return Result.success(user1);
        }

        return Result.error("短信发送失败");
    }

    /*
    用户退出登录
     */
    @PostMapping("/loginout")
    public Result<String> loginout(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return Result.success("退出成功");
    }
}
