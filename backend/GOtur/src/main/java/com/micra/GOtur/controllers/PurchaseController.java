package com.micra.GOtur.controllers;

import com.micra.GOtur.models.Food;
import com.micra.GOtur.models.Ingredient;
import com.micra.GOtur.models.Purchase;
import com.micra.GOtur.models.PurchaseItem;
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
@RequestMapping("/purchase")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PurchaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/addSinglePurchaseTest")
    public ResponseEntity<String> addSinglePurchase(@RequestBody Purchase purchase) {
        String sql = "INSERT INTO Purchase(customer_id, address_id, restaurant_id, customer_note, is_group_purchase, total_price) VALUES (?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchase.getCustomer_id(), purchase.getAddress_id(), purchase.getRestaurant_id(),
                purchase.getCustomer_note(), Boolean.FALSE, 0f);

        return new ResponseEntity<>("Purchase Has Successfully Formed!", HttpStatus.OK);
    }

    @PostMapping("/addFoodWithIngredient/{customerId}")
    public ResponseEntity<String> addPurchaseItemToPurchase(@PathVariable("customerId") int customerId,
                                                            @RequestBody PurchaseItem purchaseItem) {
        Food food = purchaseItem.getFood();
        List<Ingredient> ingredientList = purchaseItem.getIngredientList();
        int restaurant_id = food.getRestaurant_id();

        // check if the customer already has an unpaid single purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        int purchase_id = -1; // will be changed separately in the blocks
        if (exists) { // the customer already has an unpaid purchase, check if the restaurant names match
            String restaurantSql = "SELECT P.restaurant_id FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0 AND P.is_paid = 0;";
            int existing_restaurant_id = jdbcTemplate.queryForObject(restaurantSql, Integer.class, customerId);

            if ( restaurant_id != existing_restaurant_id ) {
                return new ResponseEntity<>("Customer With ID: " + customerId + " Already Has An Unpaid Purchase From Another Restaurant!", HttpStatus.BAD_REQUEST);
            }

            String purchaseSql = "SELECT P.purchase_id FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0 AND P.is_paid = 0;";
            purchase_id = jdbcTemplate.queryForObject(purchaseSql, Integer.class, customerId);
        }
        else { // insert a new purchase
            SimpleJdbcInsert insertIntoRestaurant = new SimpleJdbcInsert(jdbcTemplate).withTableName("Purchase").usingColumns("customer_id", "restaurant_id", "is_group_purchase").usingGeneratedKeyColumns("purchase_id");
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("customer_id", customerId);
            parameters.put("restaurant_id", restaurant_id);
            parameters.put("is_group_purchase", Boolean.FALSE);

            // Get the inserted id back
            purchase_id = insertIntoRestaurant.executeAndReturnKey(parameters).intValue();
        }

        // check if the food is already contained in the purchase
        int food_id = food.getFood_id();
        String checkFoodSql = "SELECT EXISTS (SELECT * FROM FoodInPurchase F WHERE F.purchase_id = ? AND F.food_id = ?);";
        boolean existsFood = jdbcTemplate.queryForObject(checkFoodSql, Boolean.class, purchase_id, food_id);

        if (existsFood) { // if the food is already contained in the purchase
            return new ResponseEntity<>("The Food Is Already Contained In The Purchase!", HttpStatus.BAD_REQUEST);
        }

        // insert the food to FoodInPurchase
        String insertFood = "INSERT INTO FoodInPurchase(food_id, purchase_id) VALUES (?, ?);";
        System.out.println(">>" + insertFood);
        jdbcTemplate.update(insertFood, food_id, purchase_id);

        for (Ingredient ingredient : ingredientList) {
            // insert the ingredient to IngredientInPurchase
            int ingredient_id = ingredient.getIngredient_id();
            String insertIngredient = "INSERT INTO IngredientInPurchase(ingredient_id, purchase_id) VALUES (?, ?);";
            System.out.println(">>" + insertIngredient);
            jdbcTemplate.update(insertIngredient, ingredient_id, purchase_id);
        }

        return new ResponseEntity<>("Purchase Has Successfully Formed!", HttpStatus.OK);
    }
}
