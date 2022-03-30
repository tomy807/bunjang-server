package com.example.demo.src.favorite;


import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.GetMenuRes;
import com.example.demo.src.favorite.model.GetFavoriteRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class FavoriteProvider {

    private final FavoriteDao favoriteDao;


    public int checkIsFavorite(int productIdx, int userIdx) throws BaseException {
        try{
            return favoriteDao.checkIsFavorite(productIdx, userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkDeletedFavorite(int productIdx, int userIdx) throws BaseException {
        try{
            return favoriteDao.checkDeletedFavorite(productIdx, userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFavoriteRes> getFavorites(int userIdx) throws BaseException {
        try{
            return favoriteDao.getFavorites(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
