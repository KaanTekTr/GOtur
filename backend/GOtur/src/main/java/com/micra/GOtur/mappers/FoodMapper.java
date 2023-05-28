package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Food;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodMapper implements RowMapper<Food> {
    @Override
    public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
        int food_id = rs.getInt("food_id");
        int food_category_id = rs.getInt("food_category_id");
        int restaurant_id = rs.getInt("restaurant_id");
        int menu_category_id = rs.getInt("menu_category_id");
        String food_name = rs.getString("food_name");
        String fixed_ingredients = rs.getString("fixed_ingredients");
        int price = rs.getInt("price");

        Food food = new Food();
        food.setFood_id(food_id);
        food.setFood_category_id(food_category_id);
        food.setRestaurant_id(restaurant_id);
        food.setMenu_category_id(menu_category_id);
        food.setFood_name(food_name);
        food.setFixed_ingredients(fixed_ingredients);
        food.setPrice(price);

        return food;
    }
}
