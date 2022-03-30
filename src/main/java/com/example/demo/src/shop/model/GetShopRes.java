package com.example.demo.src.shop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetShopRes {

    private String userImageUrl;
    private String shopName;
    private String userName;
    private String introduction;
    private Integer rate;
    private Integer openDay;
    private Integer productCount;
    private Integer reviewCount;
    private Integer inquiryCount;
    private Integer followingCount;
    private Integer followerCount;
    private List<GetProductByShopRes> products;
    private List<GetReviewByShopRes> reviews;
    private List<GetInquiryByShopRes> inquiries;

}
