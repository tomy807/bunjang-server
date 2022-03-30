package com.example.demo.src.shop;


import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.shop.model.*;
import com.example.demo.src.user.model.GetMyPageRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ShopDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetShopRes getShop(int userIdx) {
        String getShopResQuery = "select profile_Url, shop_name,user_name,introduction,\n" +
                "       case when rate is not null then rate else 0 end as rate,\n" +
                "       (case when timestampdiff(day , Users.createdAt, current_timestamp) <1\n" +
                "                then 0\n" +
                "            else datediff( current_timestamp, Users.createdAt)\n" +
                "        end) as openDay,\n" +
                "       case when productCount is not null then productCount else 0 end as productCount,\n" +
                "       case when reviewCount is not null then reviewCount else 0 end as reviewCount,\n" +
                "       case when inquiryCount is not null then inquiryCount else 0 end as inquiryCount,\n" +
                "       case when followerCount is not null then followerCount else 0 end as followerCount,\n" +
                "       case when followingCount is not null then followingCount else 0 end as followingCount\n" +
                "from Users\n" +
                "         left join (select user_id, count(*) as followerCount from Following group by user_id) as Follower\n" +
                "                   on Users.user_id = Follower.user_id\n" +
                "         left join (select following_user_id, count(*) as followingCount from Following group by following_user_id) as Following\n" +
                "                   on Users.user_id = Following.following_user_id\n" +
                "         left join (select store_id, ROUND(SUM(rate) / COUNT(store_id), 1) as rate,count(*) reviewCount from Reviews group by store_id) as R\n" +
                "                   on R.store_id = Users.user_id\n" +
                "         left join (select user_id,count(*) as productCount from Products where sell_status='SELLING'  group by user_id) as P\n" +
                "                   on P.user_id=Users.user_id\n" +
                "         left join (select P2.user_id,count(*) as inquiryCount from ProductInquiry inner join Products P2 on ProductInquiry.product_id = P2.product_id) as PI\n" +
                "                   on PI.user_id=Users.user_id\n" +
                "where Users.user_id = ?";
        return this.jdbcTemplate.queryForObject(getShopResQuery,
                (rs, rowNum) -> new GetShopRes(
                        rs.getString("profile_Url"),
                        rs.getString("shop_name"),
                        rs.getString("user_name"),
                        rs.getString("introduction"),
                        rs.getInt("rate"),
                        rs.getInt("openDay"),
                        rs.getInt("productCount"),
                        rs.getInt("reviewCount"),
                        rs.getInt("inquiryCount"),
                        rs.getInt("followingCount"),
                        rs.getInt("followerCount"), null, null, null),
                userIdx);
    }

    public List<GetProductByShopRes> getProducts(int shopIdx,int userIdx) {
        String getProductsQuery = "select Products.product_id as productIdx," +
                " product_title as title," +
                " price,\n" +
                "       (select product_image_url from ProductImages where Products.product_id = ProductImages.product_id limit 1) as productImg,\n"+
                "       secure_payment as securePayment\n," +
                "       (case when F.user_id=? then 'LIKE' else 'UNLIKE' end) as myFavorite\n" +
                "from Products\n" +
                "left join (select product_id, user_id, count(*) as fCount from Favorites group by product_id) as F on Products.product_id = F.product_id\n" +
                "where Products.sell_status='SELLING' and Products.user_id=?";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductByShopRes(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("productImg"),
                        rs.getString("securePayment"),
                        rs.getString("myFavorite")
                ), userIdx, shopIdx);
    }

    public List<GetInquiryByShopRes> getInquiries(int shopIdx) {
        String getProductsQuery = "select ProductInquiry.user_id as userIdx,\n" +
                "       text,\n" +
                "       U.shop_name as userName,\n" +
                "       U.profile_Url as userProfileUrl,\n" +
                "       (case when timestampdiff(second , ProductInquiry.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, ProductInquiry.createdAt, current_timestamp),' 초 전')\n" +
                "            when timestampdiff(minute , ProductInquiry.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, ProductInquiry.createdAt, current_timestamp),' 분 전')\n" +
                "            when timestampdiff(hour, ProductInquiry.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, ProductInquiry.createdAt, current_timestamp),' 시간 전')\n" +
                "            else concat(datediff( current_timestamp, ProductInquiry.createdAt),' 일 전')\n" +
                "        end) as createdAt\n" +
                "from ProductInquiry\n" +
                "         inner join Users U on ProductInquiry.user_id = U.user_id\n" +
                "         left join Products P on ProductInquiry.product_id = P.product_id\n" +
                "         left join Users U2 on P.user_id = U2.user_id\n" +
                "where U2.user_id=?";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetInquiryByShopRes(
                        rs.getInt("userIdx"),
                        rs.getString("text"),
                        rs.getString("userName"),
                        rs.getString("userProfileUrl"),
                        rs.getString("createdAt")
                ),shopIdx);
    }

    public List<GetReviewByShopRes> getReviews(int shopIdx) {
        String getReviewsQuery = "select U.user_id as userIdx,\n" +
                "       profile_Url,\n" +
                "       rate,\n" +
                "       text,\n" +
                "       product_title,\n" +
                "       (case when timestampdiff(second , Reviews.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, Reviews.createdAt, current_timestamp),' 초 전')\n" +
                "            when timestampdiff(minute , Reviews.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, Reviews.createdAt, current_timestamp),' 분 전')\n" +
                "            when timestampdiff(hour, Reviews.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, Reviews.createdAt, current_timestamp),' 시간 전')\n" +
                "            else concat(datediff( current_timestamp, Reviews.createdAt),' 일 전')\n" +
                "        end) as createdAt\n" +
                "from Reviews\n" +
                "         inner join Users U on Reviews.user_id = U.user_id\n" +
                "         left join Products P on Reviews.product_id = P.product_id where store_id=?";
        return jdbcTemplate.query(getReviewsQuery,
                (rs, rowNum) -> new GetReviewByShopRes(
                        rs.getInt("userIdx"),
                        rs.getString("profile_Url"),
                        rs.getInt("rate"),
                        rs.getString("text"),
                        rs.getString("product_title"),
                        rs.getString("createdAt")
                ), shopIdx);
    }

    public List<GetFollowingByShopRes> getFollowings(int shopIdx) {
        String getReviewsQuery = "select Following.user_id as userIdx,\n" +
                "       U.shop_name as shopName,\n" +
                "       profile_Url,\n" +
                "       productCount,\n" +
                "       followerCount\n" +
                "from Following\n" +
                "         inner join Users U on Following.user_id = U.user_id\n" +
                "         left join (select count(*) as productCount, sell_status, user_id from Products where sell_status = 'SELLING' group by user_id) P on P.user_id = U.user_id\n" +
                "         left join (select user_id,count(*) as followerCount from Following group by user_id) as F2 on F2.user_id=U.user_id\n" +
                "where Following.following_user_id = ? \n" +
                "group by U.user_id";
        return jdbcTemplate.query(getReviewsQuery,
                (rs, rowNum) -> new GetFollowingByShopRes(
                        rs.getInt("userIdx"),
                        rs.getString("profile_Url"),
                        rs.getString("shopName"),
                        rs.getInt("productCount"),
                        rs.getInt("followerCount"),
                        null
                ), shopIdx);

    }

    public List<GetFollowerByShopRes> getFollowers(int shopIdx) {
        String getReviewsQuery = "select Following.following_user_id as userIdx,\n" +
                "       U.profile_Url as userImageUrl,\n" +
                "       U.shop_name as shopName,\n" +
                "       case when productCount is not null then productCount else 0 end as productCount,\n" +
                "       case when count(F.following_user_id) is not null then count(F.following_user_id) else 0 end as followerCount\n" +
                "from Following\n" +
                "         left join Users U on Following.following_user_id = U.user_id\n" +
                "         left join (select count(*) as productCount, sell_status, user_id\n" +
                "                    from Products\n" +
                "                    where sell_status = 'SELLING'\n" +
                "                    group by user_id) as P2 on U.user_id = P2.user_id\n" +
                "         left join Following F on Following.following_user_id = F.user_id\n" +
                "where Following.user_id = ?\n" +
                "group by U.user_id";
        return jdbcTemplate.query(getReviewsQuery,
                (rs, rowNum) -> new GetFollowerByShopRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImageUrl"),
                        rs.getString("shopName"),
                        rs.getInt("productCount"),
                        rs.getInt("followerCount")
                ), shopIdx);

    }

    public int checkProducts(int shopIdx) {
        String checkProductsQuery = "select exists(select * from Products where user_id = ? and sell_status = 'SELLING')";
        return this.jdbcTemplate.queryForObject(checkProductsQuery, int.class, shopIdx);
    }
}
