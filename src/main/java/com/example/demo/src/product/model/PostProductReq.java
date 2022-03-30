package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReq {
    private List<ProductImg> productImgList;
    private String title;
    private int categoryLarge;
    private int categoryMiddle;
    private int categorySmall;
    private Integer price;
    private List<ProductTag> productTagList;
    private String explanation;
    private String shippingFee;
    private int quantity;
    private String productStatus;
    private String exchangePossible;
    private String securePayment;
}
