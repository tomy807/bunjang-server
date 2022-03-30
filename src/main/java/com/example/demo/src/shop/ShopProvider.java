package com.example.demo.src.shop;

import com.example.demo.config.BaseException;
import com.example.demo.src.shop.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ShopProvider {

    private final ShopDao shopDao;

    public GetShopRes getShop(int userIdx,int shopIdx) throws BaseException {
        try{
            List<GetProductByShopRes> products = shopDao.getProducts(shopIdx, userIdx);
            List<GetReviewByShopRes> reviews = shopDao.getReviews(shopIdx);
            List<GetInquiryByShopRes> inquiries = shopDao.getInquiries(shopIdx);
            GetShopRes shop = shopDao.getShop(shopIdx);
            shop.setProducts(products);
            shop.setReviews(reviews);
            shop.setInquiries(inquiries);
            return shop;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductByShopRes> getProducts(int shopIdx,int userIdx) throws BaseException {
        try{
            return shopDao.getProducts(shopIdx,userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetInquiryByShopRes> getInquiries(int shopIdx) throws BaseException {
        try{
            return shopDao.getInquiries(shopIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewByShopRes> getReviews(int shopIdx) throws BaseException {
        try{
            return shopDao.getReviews(shopIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowingByShopRes> getFollowings(int shopIdx,int userIdx) throws BaseException {
        try{

            List<GetFollowingByShopRes> followings = shopDao.getFollowings(shopIdx);
            for (GetFollowingByShopRes following : followings) {
                int followingUserIdx = following.getUserIdx();
                following.setProducts(shopDao.getProducts(followingUserIdx,userIdx));
            }
            return followings;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowerByShopRes> getFollowers(int shopIdx) throws BaseException {
        try{
            return shopDao.getFollowers(shopIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProducts(int shopIdx) throws BaseException {
        try{
            return shopDao.checkProducts(shopIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
