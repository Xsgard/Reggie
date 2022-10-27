package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import com.itheima.reggie.utils.mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        String email = phone + "@163.com";
        if (StringUtils.isNotEmpty(phone)) {
            //生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String msg = "【您的验证码为】:" + code + "。验证码提供他人可能会造成账号被盗，请勿转发或泄露！";
            log.info("验证码:{}", code);
            //发送验证码
//            mail.sendMail("【验证码】", msg, email);
            //需要将生成的验证码保存到Session，用以校验
            session.setAttribute(phone, code);
            return R.success("验证码发送成功");
        }

        return R.error("验证码发送失败，请稍后再试！");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取session中保存的验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeSaved = session.getAttribute(phone);
        //比对验证码
        if (codeSaved != null && codeSaved.equals(code)) {
            //判断用户是否完成注册，新用户自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //新用户注册
                user = new User();
                user.setName("reggie" + code);
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.removeAttribute(phone);
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
