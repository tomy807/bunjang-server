package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FollowingProduct {

    private Integer productIdx;
    private String productImgUrl;
    private Integer price;
}
