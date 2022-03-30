package com.example.demo.src.favorite;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final FavoriteProvider favoriteProvider;

    public String exchangeFavorite(int productIdx, int userIdx) throws BaseException {
        try {

            int favorite = favoriteProvider.checkIsFavorite(productIdx, userIdx);

            if (favorite == 0) {
                if (favoriteProvider.checkDeletedFavorite(productIdx, userIdx) == 1) {
                    favoriteDao.recreateFavorite(productIdx, userIdx);
                    return "찜목록에 추가했습니다.";
                }
                favoriteDao.createFavorite(productIdx, userIdx);
                return "찜목록에 추가했습니다.";
            } else if (favorite == 1) {
                favoriteDao.deleteFavorite(productIdx, userIdx);
                return "찜목록에서 삭제했습니다.";
            }
            return "";
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
