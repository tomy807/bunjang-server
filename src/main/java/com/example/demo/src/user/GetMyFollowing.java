package com.example.demo.src.user;

import com.example.demo.src.user.model.FollowingProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetMyFollowing {

    private Integer followingUserIdx;
    private String followingShopName;
    private String profileUrl;
    private Integer productCount;
    private Integer followerCount;
    private List<FollowingProduct> followingProducts;
}
