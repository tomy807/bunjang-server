package com.example.demo.src.recent;

import com.example.demo.config.BaseException;
import com.example.demo.src.favorite.model.GetFavoriteRes;
import com.example.demo.src.recent.model.GetRecentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class RecentProvider {

    private final RecentDao recentDao;


    public List<GetRecentRes> getRecents(int userIdx) throws BaseException {
        try{
            return recentDao.getRecents(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
