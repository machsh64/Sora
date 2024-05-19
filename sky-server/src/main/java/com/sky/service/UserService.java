package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-19 21:54
 * @description:
 **/
public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
