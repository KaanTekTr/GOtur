package com.micra.GOtur.mappers;

import com.micra.GOtur.models.RestaurantOwner;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantOwnerMapper implements RowMapper<RestaurantOwner> {

    @Override
    public RestaurantOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
        int user_id = rs.getInt("user_id");
        String username = rs.getString("username");
        String hashed_password = rs.getString("hashed_password");
        String password_salt = rs.getString("password_salt");
        String email = rs.getString("email");
        String phone_number = rs.getString("phone_number");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        int restaurant_count = rs.getInt("restaurant_count");

        RestaurantOwner restaurantOwner = new RestaurantOwner();
        restaurantOwner.setUser_id(user_id);
        restaurantOwner.setUsername(username);
        restaurantOwner.setHashed_password(hashed_password);
        restaurantOwner.setPassword_salt(password_salt);
        restaurantOwner.setEmail(email);
        restaurantOwner.setPhone_number(phone_number);
        restaurantOwner.setAge(age);
        restaurantOwner.setGender(gender);
        restaurantOwner.setRestaurant_count(restaurant_count);

        return restaurantOwner;
    }
}
