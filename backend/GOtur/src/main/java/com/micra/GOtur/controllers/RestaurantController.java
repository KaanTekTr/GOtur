package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.*;
import com.micra.GOtur.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @GetMapping("/restaurantsForCustomer/{customerId}")
    public List<RestaurantView> getRestaurantsForCustomer(
            @PathVariable("customerId") int customerId
    ) {
        String sql = "SELECT * FROM restaurants_for_customer;";
        List<RestaurantView> list = jdbcTemplate.query(sql, new RestaurantViewMapper());

        String currDistrict = "SELECT district FROM User U NATURAL JOIN Address A WHERE (U.user_id = ? AND A.customer_id = U.user_id)";
        String district = jdbcTemplate.queryForObject(currDistrict, String.class, customerId);
        // System.out.println(district);
        List<RestaurantView> newList = new ArrayList<>();
        for (RestaurantView rs : list) {
            if (Objects.equals(rs.getDistrict(), district)) {
                newList.add(rs);
            }
        }

        return newList;
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

    @GetMapping("/ingredients/{foodId}")
    public List<Ingredient> getIngredientsByFoodId(@PathVariable("foodId") int foodId) {
        String sql = "SELECT * FROM Ingredient I WHERE I.food_id = ?;";

        List<Ingredient> list = jdbcTemplate.query(sql, new IngredientMapper(), foodId);

        return list;
    }

    @GetMapping("/getAllNonDeliveredPurchases/{restaurantId}")
    public List<Purchase> getAllNonDeliveredPurchasesOfRestaurant(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM Purchase P WHERE P.restaurant_id = ? AND P.is_paid = 1 AND P.is_delivered = 0;";

        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(), restaurantId);

        return  list;
    }

    @GetMapping("/getAllDepartedNonDeliveredPurchases/{restaurantId}")
    public List<Purchase> getAllDepartedNonDeliveredPurchasesOfRestaurant(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM Purchase P WHERE P.restaurant_id = ? AND P.is_paid = 1 AND P.is_departed = 1 AND P.is_delivered = 0;";

        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(), restaurantId);

        return  list;
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

    @PatchMapping("/updateRestaurant/{restaurantId}")
    public ResponseEntity<String> updateFoodByFoodId(@PathVariable("restaurantId") int restaurantId,
                                                     @RequestBody Restaurant restaurant) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, restaurantId);

        if (!exists) { // if the restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "UPDATE Restaurant R SET R.restaurant_name = ?, R.district = ?, R.open_hour = ?, R.close_hour = ?, R.min_delivery_price = ?, R.coupon_limit = ?, R.discount_percentage = ? WHERE R.restaurant_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, restaurant.getRestaurant_name(), restaurant.getDistrict(), restaurant.getOpen_hour(), restaurant.getClose_hour(), restaurant.getMin_delivery_price(), restaurant.getCoupon_limit(), restaurant.getDiscount_percentage(),restaurantId);

        return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " Has Been Successfully Updated!", HttpStatus.OK);
    }

    @PatchMapping("/updateFood/{foodId}")
    public ResponseEntity<String> updateFoodByFoodId(@PathVariable("foodId") int foodId,
                                                      @RequestParam int newFoodCategoryId,
                                                      @RequestParam int newMenuCategoryId,
                                                     @RequestParam String newFoodName,
                                                     @RequestParam String newFixedIngredients,
                                                     @RequestParam int newPrice) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Food F WHERE F.food_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, foodId);

        if (!exists) { // if food does not exist
            return new ResponseEntity<>("Food With ID: " + foodId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "UPDATE Food F SET F.food_category_id = ?, F.menu_category_id = ?, F.food_name = ?, F.fixed_ingredients = ?, F.price = ? WHERE F.food_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, newFoodCategoryId, newMenuCategoryId, newFoodName, newFixedIngredients, newPrice, foodId);

        return new ResponseEntity<>("Food With ID: " + foodId + " Has Been Successfully Updated!", HttpStatus.OK);
    }

    @PatchMapping("/updateIngredient/{ingredientId}")
    public ResponseEntity<String> updateIngredientByIngredientId(@PathVariable("ingredientId") int ingredientId,
                                                      @RequestParam String newIngredientName,
                                                      @RequestParam int newPrice) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Ingredient I WHERE I.ingredient_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, ingredientId);

        if (!exists) { // if ingredient does not exist
            return new ResponseEntity<>("Ingredient With ID: " + ingredientId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "UPDATE Ingredient I SET I.ingredient_name = ?, I.price = ? WHERE I.ingredient_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, newIngredientName, newPrice, ingredientId);

        return new ResponseEntity<>("Ingredient With ID: " + ingredientId + " Has Been Successfully Updated!", HttpStatus.OK);
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

    @DeleteMapping("/deleteIngredient/{ingredientId}")
    public ResponseEntity<String> deleteIngredientFromFoodByIngredientId(@PathVariable("ingredientId") int ingredientId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Ingredient I WHERE I.ingredient_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, ingredientId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Ingredient With ID: " + ingredientId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Ingredient I WHERE I.ingredient_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, ingredientId);

        return new ResponseEntity<>("Ingredient With ID: " + ingredientId + " Has Been Successfully Deleted!", HttpStatus.OK);
    }
}
