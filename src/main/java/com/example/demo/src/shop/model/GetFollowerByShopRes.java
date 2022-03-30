package com.example.demo.src.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowerByShopRes {

    private Integer userIdx;
    private String userImageUrl;
    private String shopName;
    private Integer productCount;
    private Integer followerCount;
}
