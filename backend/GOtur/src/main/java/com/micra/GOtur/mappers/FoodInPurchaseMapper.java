package com.micra.GOtur.mappers;

import com.micra.GOtur.models.FoodInPurchase;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodInPurchaseMapper implements RowMapper<FoodInPurchase> {
    @Override
    public FoodInPurchase mapRow(ResultSet rs, int rowNum) throws SQLException {
        int food_id = rs.getInt("food_id");
        int purchase_id = rs.getInt("purchase_id");
        int food_order = rs.getInt("food_order");

        FoodInPurchase foodInPurchase = new FoodInPurchase();
        foodInPurchase.setFood_id(food_id);
        foodInPurchase.setPurchase_id(purchase_id);
        foodInPurchase.setFood_order(food_order);

        return foodInPurchase;
    }
}
