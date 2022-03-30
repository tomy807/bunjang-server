package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SellProduct{
    private int productIdx;
    private String productImgUrl;
    private Integer price;
}
