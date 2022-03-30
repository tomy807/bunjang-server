package com.example.demo.src.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowingByShopRes {

    private Integer userIdx;
    private String userImageUrl;
    private String shopName;
    private Integer productCount;
    private Integer followerCount;
    private List<GetProductByShopRes> products;
}
