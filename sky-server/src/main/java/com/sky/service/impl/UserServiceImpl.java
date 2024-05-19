package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-19 22:04
 * @description:
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 微信登录接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务, 获得当前微信用户的openid
        String openId = getOpenId(userLoginDTO.getCode());
        // 判断openid是否为空, 如果为空表示登陆失败, 则抛出业务异常
        if (openId == null) {
            throw new LoginFailedException("微信登录失败");
        }
        // 判断当前用户是否为新用户
        User user = userMapper.selectByOpenid(openId);
        // 如果为新用户, 则调用注册接口服务, 注册用户信息
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        // 返回这个用户对象

        return user;
    }

    /**
     * 调用微信接口服务, 获得当前微信用户的openid
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        // 调用微信接口服务, 获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String body = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(body);
        String openid = jsonObject.getString("openid");
        return openid;
    }

}
