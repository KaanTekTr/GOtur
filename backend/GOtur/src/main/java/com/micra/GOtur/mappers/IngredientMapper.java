package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Ingredient;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientMapper implements RowMapper<Ingredient> {
    @Override
    public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
        int ingredient_id = rs.getInt("ingredient_id");
        int food_id = rs.getInt("food_id");
        String ingredient_name = rs.getString("ingredient_name");
        int price = rs.getInt("price");

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient_id(ingredient_id);
        ingredient.setFood_id(food_id);
        ingredient.setIngredient_name(ingredient_name);
        ingredient.setPrice(price);

        return ingredient;
    }
}
