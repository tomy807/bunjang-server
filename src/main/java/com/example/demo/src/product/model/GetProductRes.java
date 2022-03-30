package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private int productIdx;
    private String productImg;
    private String title;
    private Integer price;
    private String directAddress;
    private String securePayment;
    private String myFavorite;
    private String createdAt;
    private String favoriteCount;
}