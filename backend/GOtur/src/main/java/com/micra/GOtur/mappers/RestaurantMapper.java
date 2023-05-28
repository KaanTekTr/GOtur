package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Restaurant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantMapper implements RowMapper<Restaurant> {
    @Override
    public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
        int restaurant_id = rs.getInt("restaurant_id");
        String restaurant_name = rs.getString("restaurant_name");
        String district = rs.getString("district");
        float total_earnings = rs.getFloat("total_earnings");
        String open_hour = rs.getString("open_hour");
        String close_hour = rs.getString("close_hour");
        int min_delivery_price = rs.getInt("min_delivery_price");
        boolean is_top_restaurant = rs.getBoolean("is_top_restaurant");
        float rating = rs.getFloat("rating");

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurant_id(restaurant_id);
        restaurant.setRestaurant_name(restaurant_name);
        restaurant.setDistrict(district);
        restaurant.setTotal_earnings(total_earnings);
        restaurant.setOpen_hour(open_hour);
        restaurant.setClose_hour(close_hour);
        restaurant.setMin_delivery_price(min_delivery_price);
        restaurant.setIs_top_restaurant(is_top_restaurant);
        restaurant.setRating(rating);

        return restaurant;
    }
}
