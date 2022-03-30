package com.example.demo.src.shop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewByShopRes {
    private Integer userIdx;
    private String profileUrl;
    private Integer rate;
    private String text;
    private String productTitle;
    private String createdAt;

}
