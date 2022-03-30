package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSellRes {

    private Integer orderIdx;
    private String productImgUrl;
    private String orderStatus;
    private String title;
    private Integer price;
    private String shopName;
    private String securePayment;
    private String orderDay;
}
