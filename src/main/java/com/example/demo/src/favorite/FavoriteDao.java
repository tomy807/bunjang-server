package com.example.demo.src.favorite;


import com.example.demo.src.favorite.model.GetFavoriteRes;
import com.example.demo.src.product.model.GetProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FavoriteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createFavorite(int productIdx, int userIdx) {
        jdbcTemplate.update("insert into Favorites(user_id, product_id) VALUE (?,?)", userIdx, productIdx);
    }



    public int checkIsFavorite(int productIdx, int userIdx) {
        String checkIsFavoriteQuery = "select exists(select * from Favorites where product_id = ? and user_id = ? and status='SAVED')";
        return this.jdbcTemplate.queryForObject(checkIsFavoriteQuery, int.class, productIdx, userIdx);
    }

    public void deleteFavorite(int productIdx, int userIdx) {
        Object[] deleteFavoriteParams = new Object[]{productIdx, userIdx};
        jdbcTemplate.update("delete from Favorites where product_id=? and user_id=?", deleteFavoriteParams);
    }

    public int checkDeletedFavorite(int productIdx, int userIdx) {
        String checkIsFavoriteQuery = "select exists(select * from Favorites where product_id = ? and user_id = ? and status='DELETED')";
        return this.jdbcTemplate.queryForObject(checkIsFavoriteQuery, int.class, productIdx, userIdx);

    }

    public void recreateFavorite(int productIdx, int userIdx) {
        jdbcTemplate.update("update Favorites set status='SAVED' where product_id=? and user_id=?", userIdx, productIdx);
    }

    public List<GetFavoriteRes> getFavorites(int userIdx) {
        String getFavoritesQuery = "select P.product_id as productIdx,\n" +
                "       (select product_image_url from ProductImages where ProductImages.product_id = P.product_id limit 1) as productImg,\n" +
                "       P.product_title as title,\n" +
                "       P.price as price,\n" +
                "       case when sell_status = 'SELLING' then '판매중'\n" +
                "            when sell_status = 'RESERVED' then '예약중'\n" +
                "            when sell_status = 'SOLDOUT' then '판매완료'\n" +
                "        end as sellStatus,\n" +
                "       U.shop_name as shopName,\n" +
                "       P.secure_payment as securePayment,\n" +
                "       (case when timestampdiff(second , P.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, P.createdAt, current_timestamp),' 초 전')\n" +
                "            when timestampdiff(minute , P.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, P.createdAt, current_timestamp),' 분 전')\n" +
                "            when timestampdiff(hour, P.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, P.createdAt, current_timestamp),' 시간 전')\n" +
                "            else concat(datediff( current_timestamp, P.createdAt),' 일 전')\n" +
                "        end) as createdAt\n" +
                "from Favorites\n" +
                "         inner join Products P on Favorites.product_id = P.product_id\n" +
                "         inner join Users U on P.user_id = U.user_id\n" +
                "where Favorites.user_id=? and Favorites.status='SAVED'";
        return this.jdbcTemplate.query(getFavoritesQuery,
                (rs, rowNum) -> new GetFavoriteRes(
                        rs.getInt("productIdx"),
                        rs.getString("productImg"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("sellStatus"),
                        rs.getString("shopName"),
                        rs.getString("securePayment"),
                        rs.getString("createdAt")
                ), userIdx);
    }
}
