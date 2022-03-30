package com.example.demo.src.favorite;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.GetFavoriteRes;
import com.example.demo.src.favorite.model.PostFavoriteReq;
import com.example.demo.src.product.model.GetProductInfoRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteProvider favoriteProvider;
    private final FavoriteService favoriteService;
    private final JwtService jwtService;


    @GetMapping("")
    public BaseResponse<List<GetFavoriteRes>> getFavorites() {
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetFavoriteRes> favorites = favoriteProvider.getFavorites(userIdx);

            return new BaseResponse<>(favorites);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 하트를 눌렀을때 찜목록에 추가
     *
     * @return
     */
    @PostMapping("/{productIdx}")
    public BaseResponse<String> exchangeFavorite(@PathVariable int productIdx) {

        try {
            int userIdx = jwtService.getUserIdx();

            String result = favoriteService.exchangeFavorite(productIdx, userIdx);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
