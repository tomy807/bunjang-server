package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {

        if (checkEmail(postLoginReq.getEmail()) == 0) {
            throw new BaseException(POST_USERS_NOT_EXISTS_EMAIL);
        }
        if (checkUserStatusByEmail(postLoginReq.getEmail()) == 1) {
            throw new BaseException(DELETED_USER);
        }


        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) {
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public int checkUserStatusByUserId(int userId) throws BaseException {
        try {
            return userDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserStatusByEmail(String email) throws BaseException {
        try {
            return userDao.checkUserStatusByEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetMyPageRes getMyPage(int userIdx) throws BaseException {
        try {
            return userDao.getMyPage(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMySellingProducts getMyProducts(int userIdx, String status) throws BaseException {
        try {

            return userDao.getMyProducts(userIdx, status);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int countProductByStatus(String status, int userIdx) throws BaseException {
        try {
            return userDao.countProductByStatus(status, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMyFollowing> getFollowings(int userIdx) throws BaseException {
        try {

            List<GetMyFollowing> followings = userDao.getFollowings(userIdx);
            for (GetMyFollowing following : followings) {
                if (following.getProductCount() != 0) {
                    following.setFollowingProducts(userDao.getFollowingProducts(following.getFollowingUserIdx()));
                }
            }
            return followings;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public List<GetMyFollower> getFollowers(int userIdx) throws BaseException {
        try {

            return userDao.getFollowers(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPurchaseRes> getPurchaseList(int userIdx) throws BaseException {
        try {

            return userDao.getPurchaseList(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSellRes> getSellList(int userIdx) throws BaseException {
        try {

            return userDao.getSellList(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkOrderPurchase(int userIdx) throws BaseException {
        try {

            return userDao.checkOrderPurchase(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkOrderSell(int userIdx) throws BaseException {
        try {

            return userDao.checkOrderSell(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}