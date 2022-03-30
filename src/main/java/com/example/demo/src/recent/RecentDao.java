package com.example.demo.src.recent;

import com.example.demo.src.favorite.model.GetFavoriteRes;
import com.example.demo.src.recent.model.GetRecentRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RecentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetRecentRes> getRecents(int userIdx) {
        String getFavoritesQuery = "select P.product_id as productIdx,\n" +
                "       (select product_image_url from ProductImages where ProductImages.product_id = P.product_id limit 1) as productImg,\n" +
                "       P.product_title as title,\n" +
                "       P.price as price,\n" +
                "       case when sell_status = 'SELLING' then '판매중'\n" +
                "            when sell_status = 'RESERVED' then '예약중'\n" +
                "            when sell_status = 'SOLDOUT' then '판매완료'\n" +
                "        end as sellStatus,\n" +
                "       P.secure_payment as securePayment,\n" +
                "       (case when timestampdiff(second , P.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, P.createdAt, current_timestamp),' 초 전')\n" +
                "            when timestampdiff(minute , P.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, P.createdAt, current_timestamp),' 분 전')\n" +
                "            when timestampdiff(hour, P.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, P.createdAt, current_timestamp),' 시간 전')\n" +
                "            else concat(datediff( current_timestamp, P.createdAt),' 일 전')\n" +
                "        end) as createdAt\n" +
                "from RecentProducts\n" +
                "         inner join Products P on RecentProducts.product_id = P.product_id\n" +
                "where RecentProducts.user_id=?";
        return this.jdbcTemplate.query(getFavoritesQuery,
                (rs, rowNum) -> new GetRecentRes(
                        rs.getInt("productIdx"),
                        rs.getString("productImg"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("sellStatus"),
                        rs.getString("securePayment"),
                        rs.getString("createdAt")
                ), userIdx);

    }
}
