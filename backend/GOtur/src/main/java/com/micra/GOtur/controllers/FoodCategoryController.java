package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.FoodCategoryMapper;
import com.micra.GOtur.mappers.RestaurantOwnerMapper;
import com.micra.GOtur.mappers.ReviewMapper;
import com.micra.GOtur.models.FoodCategory;
import com.micra.GOtur.models.RestaurantOwner;
import com.micra.GOtur.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodCategory")
@CrossOrigin
public class FoodCategoryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public FoodCategoryController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<FoodCategory> getAllFoodCategories() {

        String sql = "SELECT * FROM FoodCategory F;";
        List<FoodCategory> allFoodCategories = jdbcTemplate.query(sql, new FoodCategoryMapper());
        return allFoodCategories;
    }

    @GetMapping("/{foodCategoryId}")
    public FoodCategory getFoodCategory(@PathVariable("foodCategoryId") int foodCategoryId) {
        String sql = "SELECT * FROM FoodCategory F WHERE F.food_category_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new FoodCategoryMapper(), foodCategoryId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFoodCategory(
            @RequestBody FoodCategory foodCategory
    ) {
        String sql = "INSERT INTO FoodCategory(food_category_name) VALUES (?);";
        jdbcTemplate.update(sql, foodCategory.getFood_category_name());

        return new ResponseEntity<>("Food Category with name: " + foodCategory.getFood_category_name()
                + " is successfully inserted!", HttpStatus.OK);
    }

    @PostMapping("/addToRestaurant/{foodCategoryId}/{restaurantId}")
    public ResponseEntity<String> addFoodCategoryToRestaurant(
            @PathVariable("foodCategoryId") int foodCategoryId,
            @PathVariable("restaurantId") int restaurantId
    ) {

        String checkFoodCategory = "SELECT EXISTS (SELECT * FROM FoodCategory F WHERE F.food_category_id = ?);";
        boolean existsFoodCategory = jdbcTemplate.queryForObject(checkFoodCategory, Boolean.class, foodCategoryId);
        if (!existsFoodCategory) {
            return new ResponseEntity<>("Food Category with id: " + foodCategoryId + "does not exist! Add failed!", HttpStatus.BAD_REQUEST);
        }

        String checkRestaurant = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean existsRestaurant = jdbcTemplate.queryForObject(checkRestaurant, Boolean.class, restaurantId);
        if (!existsRestaurant) {
            return new ResponseEntity<>("Restaurant with id: " + restaurantId + "does not exist! Add failed!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Serves(food_category_id, restaurant_id) VALUES (?,?);";
        jdbcTemplate.update(sql, foodCategoryId, restaurantId);

        return new ResponseEntity<>("FoodCategory with id: " + foodCategoryId +
                "is added to Restaurant with id: " + restaurantId, HttpStatus.OK);
    }

    @DeleteMapping("/delete/id/{foodCategoryId}")
    public ResponseEntity<String> deleteFoodCategoryById(
            @PathVariable("foodCategoryId") int foodCategoryId
    ) {
        String sql = "DELETE FROM FoodCategory F WHERE F.food_category_id = (?);";
        jdbcTemplate.update(sql, foodCategoryId);

        return new ResponseEntity<>("Food Category with id: " + foodCategoryId +
                " is successfully deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/name/{foodCategoryName}")
    public ResponseEntity<String> deleteFoodCategoryByName(
            @PathVariable("foodCategoryName") String foodCategoryName
    ) {
        String sql = "DELETE FROM FoodCategory F WHERE F.food_category_name = (?);";
        jdbcTemplate.update(sql, foodCategoryName);

        return new ResponseEntity<>("Food Category with name: " + foodCategoryName +
                " is successfully deleted!", HttpStatus.OK);
    }


}
