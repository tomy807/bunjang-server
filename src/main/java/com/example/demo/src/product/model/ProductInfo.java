package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductInfo{
    private String title;
    private Integer price;
    private String directAddress;
    private String productStatus;
    private String shippingFee;
    private String exchangePossible;
    private int quantity;
    private String explanation;
    private String securePayment;
    private String sellStatus;
    private String createdAt;
    private String favoriteCount;
    private int categoryId;
    private String category;
    private String productInquiry;
    private String myFavorite;
    private Integer viewCount;
}