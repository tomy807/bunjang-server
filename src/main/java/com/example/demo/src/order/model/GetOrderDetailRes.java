package com.example.demo.src.order.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderDetailRes {

    private int productId;
    private String productImg;
    private String title;
    private int price;

    private int orderId; //
    private String tradingMethod; //
    private String buyer; //
    private String seller; //
    private String shippingFee;
    private int totalPrice;
    private int year; //
    private int month; //
    private int day; //
    private String time; //

    private String address;
    private String directAddress;
    private String name;
    private String buyerPhone;
    private String sellerPhone;
}