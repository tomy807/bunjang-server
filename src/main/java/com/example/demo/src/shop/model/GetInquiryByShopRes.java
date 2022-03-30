package com.example.demo.src.shop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetInquiryByShopRes {

    private Integer userIdx;
    private String text;
    private String userName;
    private String userProfileUrl;
    private String createdAt;

}
