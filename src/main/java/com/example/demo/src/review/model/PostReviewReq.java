package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private Integer productId;
    private Double reviewRate;
    private String reviewDesc;
    private List<ReviewImg> reviewImgList;

}
