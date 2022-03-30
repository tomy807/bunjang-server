package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetMyProducts {

    private Integer productIdx;
    private String productImgUrl;
    private String title;
    private Integer price;
    private String securePayment;
    private String sellStatus;
    private String createdAt;
}
