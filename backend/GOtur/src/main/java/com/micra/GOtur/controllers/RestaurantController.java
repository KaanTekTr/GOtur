package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.FoodMapper;
import com.micra.GOtur.mappers.IngredientMapper;
import com.micra.GOtur.mappers.MenuCategoryMapper;
import com.micra.GOtur.mappers.RestaurantMapper;
import com.micra.GOtur.models.Food;
import com.micra.GOtur.models.Ingredient;
import com.micra.GOtur.models.MenuCategory;
import com.micra.GOtur.models.Restaurant;
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

    @GetMapping("/foodId/{foodId}")
    public Food getFoodByFoodId(@PathVariable("foodId") int foodId) {
        String sql = "SELECT * FROM Food F WHERE F.food_id = ?;";

        return jdbcTemplate.queryForObject(sql, new FoodMapper(), foodId);
    }

    @GetMapping("/allMenuCategories/{restaurantId}")
    public List<MenuCategory> getAllMenuCategoriesByRestaurantId(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM MenuCategory M WHERE M.restaurant_id = ?;";

        List<MenuCategory> list = jdbcTemplate.query(sql, new MenuCategoryMapper(), restaurantId);

        return list;
    }

    @GetMapping("/allFood/restaurantId/{restaurantId}")
    public List<Food> getAllFoodsByRestaurantId(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM Food F WHERE F.restaurant_id = ?;";

        List<Food> list = jdbcTemplate.query(sql, new FoodMapper(), restaurantId);

        return list;
    }

    @GetMapping("/allFood/menuCategoryId/{menuCategoryId}")
    public List<Food> getAllFoodsByMenuCategoryId(@PathVariable("menuCategoryId") int menuCategoryId) {
        String sql = "SELECT * FROM Food F WHERE F.menu_category_id = ?;";

        List<Food> list = jdbcTemplate.query(sql, new FoodMapper(), menuCategoryId);

        return list;
    }

    @GetMapping("/allFood/restaurantId/{restaurantId}/menuCategoryId/{menuCategoryId}")
    public List<Food> getAllFoodsByRestaurantIdAndMenuCategoryId(@PathVariable("restaurantId") int restaurantId,
                                                                 @PathVariable("menuCategoryId") int menuCategoryId) {
        String sql = "SELECT * FROM Food F WHERE F.restaurant_id = ? AND F.menu_category_id = ?;";

        List<Food> list = jdbcTemplate.query(sql, new FoodMapper(), restaurantId, menuCategoryId);

        return list;
    }

    @GetMapping("/ingredientId/{ingredientId}")
    public Ingredient getIngredientByIngredientId(@PathVariable("ingredientId") int ingredientId) {
        String sql = "SELECT * FROM Ingredient I WHERE I.ingredient_id = ?;";

        return jdbcTemplate.queryForObject(sql, new IngredientMapper(), ingredientId);
    }

    @PostMapping("/addMenuCategory")
    public ResponseEntity<String> addMenuCategoryByRestaurantId(@RequestBody MenuCategory menuCategory) {
        String sql = "INSERT INTO MenuCategory(restaurant_id, menu_category_name) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, menuCategory.getRestaurant_id(), menuCategory.getMenu_category_name());

        return new ResponseEntity<>("Menu Category Is Successfully Added To The Restaurant With ID: " + menuCategory.getRestaurant_id() + "!", HttpStatus.OK);
    }

    @PostMapping("/addFood")
    public ResponseEntity<String> addFoodToRestaurant(@RequestBody Food food) {
        String sql = "INSERT INTO Food(food_category_id, restaurant_id, menu_category_id, food_name, fixed_ingredients, price) VALUES (?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, food.getFood_category_id(), food.getRestaurant_id(), food.getMenu_category_id(), food.getFood_name(), food.getFixed_ingredients(), food.getPrice());

        return new ResponseEntity<>("Food Is Successfully Added To The Restaurant With ID: " + food.getRestaurant_id() + "!", HttpStatus.OK);
    }

    @PostMapping("/addIngredient")
    public ResponseEntity<String> addIngredientToFood(@RequestBody Ingredient ingredient) {
        String sql = "INSERT INTO Ingredient(food_id, ingredient_name, price) VALUES (?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, ingredient.getFood_id(), ingredient.getIngredient_name(), ingredient.getPrice());

        return new ResponseEntity<>("Ingredient Has Been Successfully Added To The Food With ID: " + ingredient.getFood_id() + "!", HttpStatus.OK);
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

    @DeleteMapping("/deleteMenuCategory/{menuCategoryId}")
    public ResponseEntity<String> deleteMenuCategoryByMenuCategoryId(@PathVariable("menuCategoryId") int menuCategoryId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM MenuCategory M WHERE M.menu_category_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, menuCategoryId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Menu Category With ID: " + menuCategoryId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM MenuCategory M WHERE M.menu_category_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, menuCategoryId);

        return new ResponseEntity<>("Menu Category With ID: " + menuCategoryId + " Is Successfully Deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteFood/{foodId}")
    public ResponseEntity<String> deleteFoodFromRestaurantByFoodId(@PathVariable("foodId") int foodId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Food F WHERE F.food_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, foodId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Food With ID: " + foodId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Food F WHERE F.food_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, foodId);

        return new ResponseEntity<>("Food With ID: " + foodId + " Has Been Successfully Deleted!", HttpStatus.OK);
    }
}
