package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.RestaurantMapper;
import com.micra.GOtur.mappers.RestaurantOwnerMapper;
import com.micra.GOtur.models.MenuCategory;
import com.micra.GOtur.models.Restaurant;
import com.micra.GOtur.models.RestaurantOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@CrossOrigin
public class RestaurantController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public RestaurantController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<Restaurant> getAllRestaurants() {
        String sql = "SELECT * FROM Restaurant R;";

        List<Restaurant> list = jdbcTemplate.query(sql, new RestaurantMapper());

        return list;
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getRestaurant(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM Restaurant R WHERE R.restaurant_id = ?;";

        return jdbcTemplate.queryForObject(sql, new RestaurantMapper(), restaurantId);
    }

    @PostMapping("/addMenuCategory")
    public ResponseEntity<String> addMenuCategoryByRestaurantId(@RequestBody MenuCategory menuCategory) {
        String sql = "INSERT INTO MenuCategory(restaurant_id, menu_category_name) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, menuCategory.getRestaurant_id(), menuCategory.getMenu_category_name());

        return new ResponseEntity<>("Menu Category Is Successfully Added To The Restaurant With ID: " + menuCategory.getRestaurant_id() + "!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{restaurantId}")
    public ResponseEntity<String> deleteRestaurantByRestaurantId(@PathVariable("restaurantId") int restaurantId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, restaurantId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // if the restaurant exists, delete it
        String deleteSql = "DELETE FROM Restaurant R WHERE R.restaurant_id = ?;";
        System.out.println(">>" + deleteSql);
        jdbcTemplate.update(deleteSql, restaurantId); // delete the restaurant

        return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " has been successfully deleted!", HttpStatus.OK);
    }
}
