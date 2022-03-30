package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Review{
    private String profileUrl;
    private String storeName;
    private String title;
    private Double rate;
    private String explanation;
    private String securePayment;
}