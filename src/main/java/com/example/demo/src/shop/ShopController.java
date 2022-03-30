package com.example.demo.src.shop;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.shop.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_SELLING_PRODUCT;

@RestController
@RequestMapping("/app/shop")
@RequiredArgsConstructor
public class ShopController {


    private final ShopProvider shopProvider;
    private final ShopService shopService;
    private final JwtService jwtService;

    @GetMapping("/{shopIdx}")
    public BaseResponse<GetShopRes> getShop(@PathVariable int shopIdx) {

        try {

            int userIdx = jwtService.getUserIdx();

            GetShopRes shop = shopProvider.getShop(userIdx,shopIdx);
            return new BaseResponse<>(shop);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{shopIdx}/products")
    public BaseResponse<List<GetProductByShopRes>> getProducts(@PathVariable int shopIdx) {
        try {

            if (shopProvider.checkProducts(shopIdx) == 0) {
                return new BaseResponse<>(NOT_EXIST_SELLING_PRODUCT);
            }

            int userIdx = jwtService.getUserIdx();

            List<GetProductByShopRes> products = shopProvider.getProducts(shopIdx, userIdx);
            return new BaseResponse<>(products);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{shopIdx}/inquiries")
    public BaseResponse<List<GetInquiryByShopRes>> getInquiries(@PathVariable int shopIdx) {
        try {


            List<GetInquiryByShopRes> products = shopProvider.getInquiries(shopIdx);
            return new BaseResponse<>(products);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{shopIdx}/reviews")
    public BaseResponse<List<GetReviewByShopRes>> getReviews(@PathVariable int shopIdx) {
        try {


            List<GetReviewByShopRes> reviews = shopProvider.getReviews(shopIdx);
            return new BaseResponse<>(reviews);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @GetMapping("/{shopIdx}/followings")
    public BaseResponse<List<GetFollowingByShopRes>> getFollowings(@PathVariable int shopIdx) {
        try {

            int userIdx = jwtService.getUserIdx();
            List<GetFollowingByShopRes> followings = shopProvider.getFollowings(shopIdx,userIdx);
            return new BaseResponse<>(followings);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{shopIdx}/followers")
    public BaseResponse<List<GetFollowerByShopRes>> getFollowers(@PathVariable int shopIdx) {
        try {


            List<GetFollowerByShopRes> followers = shopProvider.getFollowers(shopIdx);
            return new BaseResponse<>(followers);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
