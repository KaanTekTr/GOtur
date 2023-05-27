package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.RestaurantOwnerMapper;
import com.micra.GOtur.models.Restaurant;
import com.micra.GOtur.models.RestaurantOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantOwner")
@CrossOrigin
public class RestaurantOwnerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public RestaurantOwnerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("USE gotur;");
    }

    @GetMapping("/all")
    public List<RestaurantOwner> getAllRestaurantOwners() {
        String sql = "SELECT * FROM RestaurantOwner R, User U WHERE U.user_id = R.user_id;";

        List<RestaurantOwner> list = jdbcTemplate.query(sql, new RestaurantOwnerMapper());

        return list;
    }

    @GetMapping("/{restaurantOwnerId}")
    public RestaurantOwner getRestaurantOwner(@PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String sql = "SELECT * FROM RestaurantOwner R, User U WHERE U.user_id = R.user_id AND U.user_id = ?;";

        return jdbcTemplate.queryForObject(sql, new RestaurantOwnerMapper(), restaurantOwnerId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRestaurantOwner(@RequestBody RestaurantOwner restaurantOwner) {
        String sql = "INSERT INTO User(username, hashed_password, password_salt, email, phone_number, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, restaurantOwner.getUsername(), restaurantOwner.getHashed_password(), restaurantOwner.getPassword_salt(),
                restaurantOwner.getEmail(), restaurantOwner.getPhone_number(), restaurantOwner.getAge(), restaurantOwner.getGender());

        int insertedId = getIdByEmail(restaurantOwner.getEmail());

        String sql2 = "INSERT INTO RestaurantOwner(user_id) VALUES (?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, insertedId);

        return new ResponseEntity<>("Restaurant Owner Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/addRestaurant/{restaurantOwnerId}")
    public ResponseEntity<String> addRestaurantByRestaurantOwnerId(@PathVariable("restaurantOwnerId") int restaurantOwnerId,
                                                                   @RequestBody Restaurant restaurant) {
        SimpleJdbcInsert insertIntoRestaurant = new SimpleJdbcInsert(jdbcTemplate).withTableName("Restaurant").usingColumns("restaurant_name", "district",
                "open_hour", "close_hour", "min_delivery_price", "is_top_restaurant").usingGeneratedKeyColumns("restaurant_id");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("restaurant_name", restaurant.getRestaurant_name());
        parameters.put("district", restaurant.getDistrict());
        parameters.put("open_hour", restaurant.getOpen_hour());
        parameters.put("close_hour", restaurant.getClose_hour());
        parameters.put("min_delivery_price", restaurant.getMin_delivery_price());
        parameters.put("is_top_restaurant", Boolean.FALSE);

        // Get the inserted id back
        int restaurantId = insertIntoRestaurant.executeAndReturnKey(parameters).intValue();

        String sql = "INSERT INTO ManagedBy(restaurant_id, restaurant_owner_id) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, restaurantId, restaurantOwnerId); // insert to ManagedBy table

        return new ResponseEntity<>("Restaurant Successfully Inserted!", HttpStatus.OK);
    }

    public int getIdByEmail(String email) {
        String sql = "SELECT user_id FROM USER U WHERE U.email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    @DeleteMapping("/delete/{restaurantOwnerId}")
    public ResponseEntity<String> deleteRestaurantOwner(@PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String sql = "DELETE FROM USER U WHERE U.user_id = ?;";
        jdbcTemplate.update(sql, restaurantOwnerId);

        return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " has been deleted!",
                HttpStatus.OK);
    }
}
