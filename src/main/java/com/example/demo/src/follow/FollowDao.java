package com.example.demo.src.follow;

import com.example.demo.src.follow.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkStore(int storeId){
        String checkStoreQuery = "select(exists(select * from Following where following_user_id = ?))";
        int checkStoreParams = storeId;
        return this.jdbcTemplate.queryForObject(checkStoreQuery, int.class, checkStoreParams);
    }

    public int checkFollow(int userId, int storeId){
        String checkLikesQuery = "select(exists(select * from Following where user_id = ? and following_user_id = ?))";
        Object[] checkLikesParams = new Object[]{userId, storeId};
        return this.jdbcTemplate.queryForObject(checkLikesQuery, int.class, checkLikesParams);
    }

    public int checkLikesByUserId(int userId){
        String checkLikesByUserIdQuery = "select(exists(select * from LikeStore where userIdx = ?))";
        int checkLikesByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkLikesByUserIdQuery, int.class, checkLikesByUserIdParams);
    }

    public void createFollow(int userId, int storeId){
        String createFollowQuery = "insert into Following(user_id, following_user_id) values(?, ?)";
        Object[] createFollowParams = new Object[]{userId, storeId};
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);
    }

    public String checkFollowStatus(int userId, int storeId){
        String checkFollowStatusQuery = "select status from Following where user_id = ? and following_user_id = ?";
        Object[] checkFollowStatusParams = new Object[]{userId, storeId};
        return this.jdbcTemplate.queryForObject(checkFollowStatusQuery, String.class, checkFollowStatusParams);
    }

    public void activeFollowStatus(int userId, int storeId){
        String activeFollowStatusQuery = "update Following set status = 'FOLLOWING' where user_id = ? and following_user_id = ?";
        Object[] activeFollowStatusParams = new Object[]{userId, storeId};
        this.jdbcTemplate.update(activeFollowStatusQuery, activeFollowStatusParams);
    }

    public void inactiveFollowStatus(int userId, int storeId){
        String inactiveFollowStatusQuery = "update Following set status = 'UNFOLLOWING' where user_id = ? and following_user_id = ?";
        Object[] inactiveFollowStatusParams = new Object[]{userId, storeId};
        this.jdbcTemplate.update(inactiveFollowStatusQuery, inactiveFollowStatusParams);
    }

}