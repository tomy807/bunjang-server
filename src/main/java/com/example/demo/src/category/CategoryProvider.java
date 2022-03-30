package com.example.demo.src.category;


import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.GetMenuRes;
import com.example.demo.src.user.model.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class CategoryProvider {

    private final CategoryDao categoryDao;

    public List<GetMenuRes> getMenu() throws BaseException {
        try{
            List<GetMenuRes> getMenu = categoryDao.getUsers();
            return getMenu;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMenuRes> getHomeCategories() throws BaseException {
        try{
            List<GetMenuRes> homeMenu = categoryDao.getHomeCategories();
            return homeMenu;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
