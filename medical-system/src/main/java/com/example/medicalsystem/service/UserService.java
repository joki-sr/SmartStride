package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.UserDTO;
import com.example.medicalsystem.dto.UserRegistrationDTO;
import com.example.medicalsystem.exception.ConflictException;
import com.example.medicalsystem.exception.UnauthorizedException;

public interface UserService {
    /**
     * 用户登录
     * @param username 用户名/手机号
     * @param password 密码
     * @return 用户信息
     */
    UserDTO login(String username, String password) throws UnauthorizedException;
    
    /**
     * 用户注册
     * @param userDTO 用户注册信息
     * @return 注册成功的用户
     */
    UserDTO register(UserRegistrationDTO userDTO) throws ConflictException;
}
