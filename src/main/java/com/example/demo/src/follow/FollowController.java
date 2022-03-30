package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/app/follows")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowProvider followProvider;
    private final FollowService followService;
    private final JwtService jwtService;

    @Autowired
    public FollowController(FollowService followService,FollowProvider followProvider, JwtService jwtService) {
        this.followService = followService;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
    }

    /**
     * 팔로우 활성 API
     * [POST] /follows
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> pushFollow(@RequestBody PostFollowReq postFollowReq) throws BaseException {

        if(postFollowReq.getStoreId() == null){
            return new BaseResponse<>(POST_FOLLOW_EMPTY_STORE_ID);
        }
        if(followProvider.checkStore(postFollowReq.getStoreId()) == 0){
            return new BaseResponse<>(INVALID_STORE_ID);
        }
        try{
            // jwt 에서 userId 추출
            int userIdByJwt = jwtService.getUserIdx();

            if(followProvider.checkFollow(userIdByJwt, postFollowReq.getStoreId()) == 0) {
                followService.createFollow(userIdByJwt, postFollowReq.getStoreId());

            }
            else{
                followService.updateFollowStatus(userIdByJwt, postFollowReq.getStoreId());

            }
            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}