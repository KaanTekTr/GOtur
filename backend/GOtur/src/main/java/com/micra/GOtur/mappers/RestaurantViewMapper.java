package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Restaurant;
import com.micra.GOtur.models.RestaurantView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantViewMapper implements RowMapper<RestaurantView> {
    @Override
    public RestaurantView mapRow(ResultSet rs, int rowNum) throws SQLException {
        int restaurant_id = rs.getInt("restaurant_id");
        String restaurant_name = rs.getString("restaurant_name");
        String district = rs.getString("district");
        // float total_earnings = rs.getFloat("total_earnings");
        String open_hour = rs.getString("open_hour");
        String close_hour = rs.getString("close_hour");
        int min_delivery_price = rs.getInt("min_delivery_price");
        boolean is_top_restaurant = rs.getBoolean("is_top_restaurant");
        float rating = rs.getFloat("rating");

        RestaurantView restaurantView = new RestaurantView();
        restaurantView.setRestaurant_id(restaurant_id);
        restaurantView.setRestaurant_name(restaurant_name);
        restaurantView.setDistrict(district);
        // restaurant.setTotal_earnings(total_earnings);
        restaurantView.setOpen_hour(open_hour);
        restaurantView.setClose_hour(close_hour);
        restaurantView.setMin_delivery_price(min_delivery_price);
        restaurantView.setIs_top_restaurant(is_top_restaurant);
        restaurantView.setRating(rating);

        return restaurantView;
    }
}
