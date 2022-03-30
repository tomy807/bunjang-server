package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowProvider followProvider;
    private final FollowDao followDao;
    private final JwtService jwtService;

    @Autowired
    public FollowService(FollowDao followDao,  FollowProvider followProvider, JwtService jwtService) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
    }

    public void createFollow(int userId, int storeId) throws BaseException {
        try {
            followDao.createFollow(userId, storeId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateFollowStatus(int userId, int storeId) throws BaseException {
        try {
            if((followProvider.checkFollowStatus(userId, storeId)).equals("UNFOLLOWING")){
                followDao.activeFollowStatus(userId, storeId);
            }
            else if((followProvider.checkFollowStatus(userId, storeId)).equals("FOLLOWING")){
                followDao.inactiveFollowStatus(userId, storeId);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}