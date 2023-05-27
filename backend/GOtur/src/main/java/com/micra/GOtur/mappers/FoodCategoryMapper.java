package com.micra.GOtur.mappers;

import com.micra.GOtur.models.FoodCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodCategoryMapper implements RowMapper<FoodCategory> {

    @Override
    public FoodCategory mapRow(ResultSet rs, int rowNum) throws SQLException {

        int food_category_id = rs.getInt("food_category_id");
        String food_category_name = rs.getString("food_category_name");

        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setFood_category_id(food_category_id);
        foodCategory.setFood_category_name(food_category_name);

        return foodCategory;
    }
}
