package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryRes {
    private int categoryIdx;
    private String categoryUrl;
    private String categoryName;
}