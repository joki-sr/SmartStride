package com.example.medicalsystem.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    @NotBlank(message = "电话号码不能为空")
    private String phone;
    
    @NotBlank(message = "性别不能为空")
    private String gender;
    
    @NotBlank(message = "出生日期不能为空")
    private String birthDate;
    
    private String idCard;
    private String passport;
}
