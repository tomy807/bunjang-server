package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetInquiryRes {
    private int inquiryId;
    private String storeName;
    private String createdAt;
    private String text;
    private String profileUrl;
}