package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyPageRes {

    private String userImageUrl;
    private String shopName;
    private Integer rate;
    private Integer favoriteCount;
    private Integer reviewCount;
    private Integer followerCount;
    private Integer followingCount;
}
