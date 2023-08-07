package com.lncanswer.controller;

import com.lncanswer.entitly.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String bathPath;
    /*
    文件上传
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        //上传的file是一个临时文件，本次请求之后就会被释放掉 需要转存文件位置 默认存放在c盘目录下
        log.info("文件上传：{}",file);

        //拿到原始文件名 以及.后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //利用UUID工具类生成随机数， 将其与后缀拼接起来
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象 File即可用来创建文件也可用来创建目录
        File dir = new File(bathPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            //将文件转存到指定位置
            file.transferTo(new File(bathPath + fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        return Result.success(fileName);
    }

    /*
    文件下载
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(bathPath + name);
            //输出流 通过输出流将文件写回浏览器 ，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
              outputStream.write(bytes,0,len);
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
