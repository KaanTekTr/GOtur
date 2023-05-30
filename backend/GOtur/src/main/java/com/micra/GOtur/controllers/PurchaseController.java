package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.*;
import com.micra.GOtur.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/getAllPurchases/{customerId}")
    public List<Purchase> getAllPurchasesByCustomerId(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM Purchase P WHERE P.customer_id = ?;";
        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(), customerId);

        return list;
    }

    @GetMapping("/getSinglePurchases/{customerId}")
    public List<Purchase> getAllSinglePurchasesByCustomerId(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0;";
        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(), customerId);

        return list;
    }

    @GetMapping("/getPurchase/{purchaseId}")
    public Purchase getPurchasesByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String sql = "SELECT * FROM Purchase P WHERE P.purchase_id = ?";
        Purchase purchase = jdbcTemplate.queryForObject(sql, new PurchaseMapper(), purchaseId);

        return purchase;
    }

    @GetMapping("/getAllFoodAndIngredients/{purchaseId}")
    public List<PurchaseItem> getAllFoodAndIngredientsByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String sql = "SELECT * FROM FoodInPurchase FP WHERE FP.purchase_id = ?;";
        List<FoodInPurchase> foodList = jdbcTemplate.query(sql, new FoodInPurchaseMapper(), purchaseId);
        List<PurchaseItem> purchaseItemList = new ArrayList<>();

        for (FoodInPurchase foodInPurchase: foodList) {
            int food_id = foodInPurchase.getFood_id();
            int food_order = foodInPurchase.getFood_order();

            String foodSql = "SELECT * FROM Food F WHERE F.food_id = ?;";
            Food food = jdbcTemplate.queryForObject(foodSql, new FoodMapper(), food_id);

            String ingredientSql = "SELECT * FROM Ingredient I WHERE I.food_id = ? AND I.ingredient_id IN (SELECT IP.ingredient_id FROM IngredientInPurchase IP WHERE IP.purchase_id = ? AND IP.food_order = ?);";
            List<Ingredient> ingredientList = jdbcTemplate.query(ingredientSql, new IngredientMapper(), food_id, purchaseId, food_order);

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setFood(food);
            purchaseItem.setIngredientList(ingredientList);
            purchaseItemList.add(purchaseItem);
        }

        return purchaseItemList;
    }

    @GetMapping("/getAllFoodAndIngredientsUnpaidSinglePurchase/{customerId}")
    public List<PurchaseItem> getAllFoodAndIngredientsForUnpaidSinglePurchaseByPurchaseId(@PathVariable("customerId") int customerId) {
        // check if the customer has an unpaid single purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if ( !exists ) { // if the customer does not have an unpaid single purchase
            return new ArrayList<>();
        }

        // find the purchase id of an unpaid single purchase
        String findSql = "SELECT P.purchase_id FROM Purchase P WHERE P.customer_id = ? AND P.is_group_purchase = 0 AND P.is_paid = 0;";
        int purchaseId = jdbcTemplate.queryForObject(findSql, Integer.class, customerId);

        String sql = "SELECT * FROM FoodInPurchase FP WHERE FP.purchase_id = ?;";
        List<FoodInPurchase> foodList = jdbcTemplate.query(sql, new FoodInPurchaseMapper(), purchaseId);
        List<PurchaseItem> purchaseItemList = new ArrayList<>();

        for (FoodInPurchase foodInPurchase: foodList) {
            int food_id = foodInPurchase.getFood_id();
            int food_order = foodInPurchase.getFood_order();

            String foodSql = "SELECT * FROM Food F WHERE F.food_id = ?;";
            Food food = jdbcTemplate.queryForObject(foodSql, new FoodMapper(), food_id);

            String ingredientSql = "SELECT * FROM Ingredient I WHERE I.food_id = ? AND I.ingredient_id IN (SELECT IP.ingredient_id FROM IngredientInPurchase IP WHERE IP.purchase_id = ? AND IP.food_order = ?);";
            List<Ingredient> ingredientList = jdbcTemplate.query(ingredientSql, new IngredientMapper(), food_id, purchaseId, food_order);

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setFood(food);
            purchaseItem.setIngredientList(ingredientList);
            purchaseItemList.add(purchaseItem);
        }

        return purchaseItemList;
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

        // get the number of foods with that specific food_id
        int food_id = food.getFood_id();
        String checkFoodSql = "SELECT COUNT(*) FROM FoodInPurchase F WHERE F.purchase_id = ? AND F.food_id = ?;";
        int foodCount = jdbcTemplate.queryForObject(checkFoodSql, Integer.class, purchase_id, food_id);

        // insert the food to FoodInPurchase
        String insertFood = "INSERT INTO FoodInPurchase(food_id, purchase_id, food_order) VALUES (?, ?, ?);";
        System.out.println(">>" + insertFood);
        jdbcTemplate.update(insertFood, food_id, purchase_id, foodCount + 1);

        for (Ingredient ingredient : ingredientList) {
            // insert the ingredient to IngredientInPurchase
            int ingredient_id = ingredient.getIngredient_id();
            String insertIngredient = "INSERT INTO IngredientInPurchase(ingredient_id, purchase_id, food_order) VALUES (?, ?, ?);";
            System.out.println(">>" + insertIngredient);
            jdbcTemplate.update(insertIngredient, ingredient_id, purchase_id, foodCount + 1);
        }

        return new ResponseEntity<>("Purchase Has Been Successfully Formed!", HttpStatus.OK);
    }

    @PostMapping("/markBeingPrepared/{purchaseId}")
    public ResponseEntity<String> markPurchaseAsBeingPreparedByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);

        if (!exists) { // if purchase does not exist
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        if (!IsPaidByPurchaseId(purchaseId)) {
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Not Paid!", HttpStatus.BAD_REQUEST);
        }

        String checkStatusSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.being_prepared = 1);";
        boolean existsStatus = jdbcTemplate.queryForObject(checkStatusSql, Boolean.class, purchaseId);

        if (existsStatus) { // if status is already as being prepared
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Already Marked As Being Prepared!", HttpStatus.BAD_REQUEST);
        }

        // Update the status
        String sql = "UPDATE Purchase P SET P.being_prepared = 1 WHERE P.purchase_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchaseId);

        return new ResponseEntity<>("The Status Of The Purchase With ID: " + purchaseId + " Has Been Successfully Updated To Being Prepared!", HttpStatus.OK);
    }

    @PostMapping("/markDeparted/{purchaseId}")
    public ResponseEntity<String> markPurchaseAsDepartedByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);

        if (!exists) { // if purchase does not exist
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        if (!IsPaidByPurchaseId(purchaseId)) {
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Not Paid!", HttpStatus.BAD_REQUEST);
        }

        String checkStatusSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_departed = 1);";
        boolean existsStatus = jdbcTemplate.queryForObject(checkStatusSql, Boolean.class, purchaseId);

        if (existsStatus) { // if status is already as departed
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Already Marked As Departed!", HttpStatus.BAD_REQUEST);
        }

        // Update the status
        String sql = "UPDATE Purchase P SET P.is_departed = 1 WHERE P.purchase_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchaseId);

        return new ResponseEntity<>("The Status Of The Purchase With ID: " + purchaseId + " Has Been Successfully Updated To Departed!", HttpStatus.OK);
    }

    @PostMapping("/markDelivered/{purchaseId}")
    public ResponseEntity<String> markPurchaseAsDeliveredByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);

        if (!exists) { // if purchase does not exist
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        if (!IsPaidByPurchaseId(purchaseId)) {
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Not Paid!", HttpStatus.BAD_REQUEST);
        }

        String checkStatusSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_delivered = 1);";
        boolean existsStatus = jdbcTemplate.queryForObject(checkStatusSql, Boolean.class, purchaseId);

        if (existsStatus) { // if status is already delivered
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Already Marked As Delivered!", HttpStatus.BAD_REQUEST);
        }

        // Update the status
        String sql = "UPDATE Purchase P SET P.is_delivered = 1 WHERE P.purchase_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchaseId);

        return new ResponseEntity<>("The Status Of The Purchase With ID: " + purchaseId + " Has Been Successfully Updated To Delivered!", HttpStatus.OK);
    }

    public Boolean IsPaidByPurchaseId(int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_paid = 1);";
        return jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);
    }
}
