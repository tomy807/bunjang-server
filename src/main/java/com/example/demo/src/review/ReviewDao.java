package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where user_id = ? and status = 'ACTIVE')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }


    public int checkProductId( int productId){
        String checkProductIdQuery = "select(exists(select * from Products where  product_id = ? and  sell_status = 'SOLDOUT'))";
        int checkProductIdParams = productId;
        return this.jdbcTemplate.queryForObject(checkProductIdQuery, int.class, checkProductIdParams);
    }

    public int checkExistReview(int userId, int productId){
        String checkExistReviewQuery = "select(exists(select * from Reviews where user_id = ? and product_id = ?))";
        Object[] checkExistReviewParams = new Object[]{userId, productId};
        return this.jdbcTemplate.queryForObject(checkExistReviewQuery, int.class, checkExistReviewParams);
    }

    public int getStoreId(int productId){
        String getStoreIdQuery = "select user_id from Products where product_id = ? ";
        int getStoreIdParams = productId;
        return this.jdbcTemplate.queryForObject(getStoreIdQuery, int.class, getStoreIdParams);
    }

    public int createReview(int storeId, int userId, PostReviewReq postReviewReq){
        String createReviewQuery = "insert into Reviews(product_id, user_id, rate, text, store_id) values(?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{postReviewReq.getProductId(), userId, postReviewReq.getReviewRate(), postReviewReq.getReviewDesc(), storeId};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }


    public void createReviewImage(int reviewId, String reviewImageUrl){
        String createReviewImageQuery = "insert into ReviewImages(review_id, product_image_url) values(?,?)";
        Object[] createReviewImageParams = new Object[]{reviewId, reviewImageUrl};
        this.jdbcTemplate.update(createReviewImageQuery, createReviewImageParams);
    }


    public int checkReviewId(int userId){
        String checkReviewIdQuery = "select(exists(select * from Reviews where user_id = ? ))";
        int checkReviewIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkReviewIdQuery, int.class, checkReviewIdParams);
    }


    public int checkReviewIdByReviewId(int userId, int reviewId){
        String checkReviewIdQuery = "select(exists(select * from Reviews where user_id = ? and review_id = ?))";
        Object[] checkReviewIdParams = new Object[]{userId, reviewId};
        return this.jdbcTemplate.queryForObject(checkReviewIdQuery, int.class, checkReviewIdParams);
    }


    public int checkReviewImgByReviewId(int reviewId){
        String checkReviewImgByReviewIdQuery = "select(exists(select * from ReviewImages where review_id = ? ))";
        int checkReviewImgByReviewIdParams = reviewId;
        return this.jdbcTemplate.queryForObject(checkReviewImgByReviewIdQuery, int.class, checkReviewImgByReviewIdParams);
    }


    public void deleteReviewImgByReviewId(int reviewId){
        String deleteReviewImgByReviewIdQuery = "delete from ReviewImages where review_id=?";
        int deleteReviewImgByReviewIdParams = reviewId;
        this.jdbcTemplate.update(deleteReviewImgByReviewIdQuery, deleteReviewImgByReviewIdParams);
    }

    public void deleteReview(int reviewId){
        String deleteCartQuery = "delete from Reviews where review_id = ?";
        int deleteCartParams = reviewId;
        this.jdbcTemplate.update(deleteCartQuery, deleteCartParams);
    }


}