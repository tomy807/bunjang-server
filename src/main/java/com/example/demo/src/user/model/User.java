package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Integer userIdx;
    private String userName;
    private String shopName;
    private String email;
    private String password;
    private String phone;
    private String profileUrl;
    private Integer point;
    private String introduction;
    private String gender;
    private String  birthDate;
}
