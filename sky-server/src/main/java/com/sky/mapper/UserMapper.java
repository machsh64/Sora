package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @program: sky-take-out
 * @author: Ren  https://github.com/machsh64
 * @create: 2024-05-19 22:13
 * @description:
 **/
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User selectByOpenid(String openid);

    /**
     * 新增用户信息
     * @param user
     */
    void insert(User user);
}
