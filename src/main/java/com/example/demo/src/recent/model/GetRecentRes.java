package com.example.demo.src.recent.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecentRes {
    private Integer productIdx;
    private String productImg;
    private String title;
    private Integer price;
    private String sellStatus;
    private String securePayment;
    private String createdAt;
}
