package com.example.demo.src.category;

import com.example.demo.src.category.model.Category;
import com.example.demo.src.category.model.GetMenuRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetMenuRes> getUsers() {

        List<GetMenuRes> menu = new ArrayList<>();

        GetMenuRes getMenuRes1 = new GetMenuRes();
        getMenuRes1.setIconType("CATEGORY");
        List<Category> categories = jdbcTemplate.query("select * from CategoryLarge where icon_type='CATEGORY'",
                (rs, rowNum) -> new Category(
                        rs.getInt("category_large_id"),
                        rs.getString("category_large_name"),
                        rs.getString("category_icon_image_Url")));
        getMenuRes1.setCategory(categories);
        menu.add(getMenuRes1);

        GetMenuRes getMenuRes2 = new GetMenuRes();
        getMenuRes2.setIconType("LIFE");
        List<Category> categories2 = jdbcTemplate.query("select * from CategoryLarge where icon_type='LIFE'",
                (rs, rowNum) -> new Category(
                        rs.getInt("category_large_id"),
                        rs.getString("category_large_name"),
                        rs.getString("category_icon_image_Url")));
        getMenuRes2.setCategory(categories2);
        menu.add(getMenuRes2);

        return menu;
    }


    public List<GetMenuRes> getHomeCategories() {
        List<GetMenuRes> homeMenu = new ArrayList<>();

        GetMenuRes getMenuRes1 = new GetMenuRes();
        getMenuRes1.setIconType("MENU");
        List<Category> categories = jdbcTemplate.query("select * from CategoryLarge where icon_type='MENU'",
                (rs, rowNum) -> new Category(
                        rs.getInt("category_large_id"),
                        rs.getString("category_large_name"),
                        rs.getString("category_icon_image_Url")));
        getMenuRes1.setCategory(categories);
        homeMenu.add(getMenuRes1);
        return homeMenu;
    }
}
