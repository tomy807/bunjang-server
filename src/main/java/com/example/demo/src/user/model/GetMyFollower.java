package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyFollower {

    private Integer followerUserIdx;
    private String followerShopName;
    private String profileUrl;
    private Integer productCount;
    private Integer followerCount;

}
