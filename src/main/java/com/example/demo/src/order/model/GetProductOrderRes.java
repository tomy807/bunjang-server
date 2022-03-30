package com.example.demo.src.order.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductOrderRes {
     private int point;
     private String address;
    private String detailAddress;
    private String name;
     private String phone;
    private int price;
    private String title;
    private String productImg;
    private String shippingFee;
}