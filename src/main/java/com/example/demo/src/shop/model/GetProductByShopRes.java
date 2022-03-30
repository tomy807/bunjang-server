package com.example.demo.src.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetProductByShopRes {

    private int productIdx;
    private String title;
    private int price;
    private String productImg;
    private String securePayment;
    private String myFavorite;
}
