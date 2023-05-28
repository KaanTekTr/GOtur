package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Promote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PromoteMapper implements RowMapper<Promote> {

    @Override
    public Promote mapRow(ResultSet rs, int rowNum) throws SQLException {
        int promotion_id = rs.getInt("promotion_id");
        int promoter_id = rs.getInt("promoter_id");
        int restaurant_id = rs.getInt("restaurant_id");
        String promotion_code = rs.getString("promotion_code");
        int profit_rate = rs.getInt("profit_rate");
        LocalDate expiration_date = rs.getDate("expiration_date").toLocalDate();
        int user_quota = rs.getInt("user_quota");

        Promote promote = new Promote();
        promote.setPromotion_id(promotion_id);
        promote.setPromoter_id(promoter_id);
        promote.setRestaurant_id(restaurant_id);
        promote.setPromotion_code(promotion_code);
        promote.setProfit_rate(profit_rate);
        promote.setExpiration_date(expiration_date);
        promote.setUser_quota(user_quota);

        return promote;

    }
}
