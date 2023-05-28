package com.micra.GOtur.mappers;

import com.micra.GOtur.models.MenuCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuCategoryMapper implements RowMapper<MenuCategory> {
    @Override
    public MenuCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        int restaurant_id = rs.getInt("restaurant_id");
        int menu_category_id = rs.getInt("menu_category_id");
        String menu_category_name = rs.getString("menu_category_name");

        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setRestaurant_id(restaurant_id);
        menuCategory.setMenu_category_id(menu_category_id);
        menuCategory.setMenu_category_name(menu_category_name);

        return menuCategory;
    }
}
