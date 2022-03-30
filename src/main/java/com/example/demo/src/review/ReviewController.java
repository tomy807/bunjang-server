package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewService reviewService;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewController(ReviewService reviewService, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewService = reviewService;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    /**
     * 리뷰 작성 API
     * [POST] /reviews
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postReviews(@RequestBody PostReviewReq postReviewReq) {
        if(postReviewReq.getProductId() == null){
            return new BaseResponse<>(POST_REVIEWS_EMPTY_PRODUCT_ID);
        }

        if(!(postReviewReq.getReviewRate() >= 1 && postReviewReq.getReviewRate() <= 5)){
            return new BaseResponse<>(POST_REVIEWS_INVALID_SCORE);
        }

        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if (reviewProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if(reviewProvider.checkProductId(postReviewReq.getProductId()) == 0){
                return new BaseResponse<>(INVALID_PRODUCT_ID);
            }

            if(reviewProvider.checkExistReview(userIdByJwt, postReviewReq.getProductId()) == 1){
                return new BaseResponse<>(EXISTS_REVIEW);
            }

            int storeId = reviewProvider.getStoreId(postReviewReq.getProductId());

            int reviewId = reviewService.createReview(storeId, userIdByJwt, postReviewReq);

            if(postReviewReq.getReviewImgList() != null){
                List<ReviewImg> reviewImgList = postReviewReq.getReviewImgList();
                for(ReviewImg reviewImg : reviewImgList){
                    reviewService.createReviewImage(reviewId, reviewImg.getReviewImgUrl());
                }
            }

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 리뷰 삭제 API
     * [DELETE] /reviews/:reviewId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable(required = false) String reviewId) {
        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if (reviewProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }


            if(reviewProvider.checkReviewId(userIdByJwt) == 0){
                return new BaseResponse<>(EMPTY_REVIEW);
            }
            if(!isRegexInteger(reviewId)){
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }
            int id = Integer.parseInt(reviewId);
            if(id < 1){
                return new BaseResponse<>(DELETE_REVIEWS_INVALID_REVIEW_ID);
            }
            if(reviewProvider.checkReviewIdByReviewId(userIdByJwt, id) == 0){
                return new BaseResponse<>(INVALID_REVIEW_ID);
            }
            reviewService.deleteReview(userIdByJwt, id);
            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}