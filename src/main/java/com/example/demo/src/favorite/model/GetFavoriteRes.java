package com.example.demo.src.favorite.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteRes {

    private Integer productIdx;
    private String productImg;
    private String title;
    private Integer price;
    private String sellStatus;
    private String shopName;
    private String securePayment;
    private String createdAt;

}
