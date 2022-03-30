package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private Integer productId;
    private Integer point;
    private Integer tax;
    private String tradingMethod;
    private String payMethod;
    private String addressOption;
}