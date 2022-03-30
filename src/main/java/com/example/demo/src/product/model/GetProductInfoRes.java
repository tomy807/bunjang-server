package com.example.demo.src.product.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GetProductInfoRes {
    @NonNull private int productIdx;
    private ProductInfo productInfo;
    private List<ProductTag> productTagList;
    private List<ProductImg> productImgList;
    private StoreInfo storeInfo;
    private List<SellProduct> sellProductList;
    private List<Review> reviewList;
    private List<RelateProduct> relateProductList;


}
