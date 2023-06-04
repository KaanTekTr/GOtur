package com.micra.GOtur.controllers;

import com.micra.GOtur.helpers.HashPasswordHelper;
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

    @GetMapping("/restaurants/{restaurantOwnerId}")
    public List<Restaurant> getRestaurantsOfRestaurantOwner(@PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String sql = "SELECT * FROM Restaurant R WHERE R.restaurant_id IN (SELECT M.restaurant_id FROM ManagedBy M WHERE M.restaurant_owner_id = ?);";

        List<Restaurant> list = jdbcTemplate.query(sql, new RestaurantMapper(), restaurantOwnerId);

        return list;
    }

    @GetMapping("/orders/{restaurantOwnerId}")
    public List<Purchase> getOrdersOfAllRestaurants(@PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String sql = "SELECT * FROM ManagedBy M, Purchase P, RestaurantOwner R WHERE R.user_id = M.restaurant_owner_id AND P.restaurant_id = M.restaurant_id AND P.is_paid = 1 AND P.being_prepared = 1 AND R.user_id = ?;";
        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(),restaurantOwnerId );
        return  list;
    }

    @GetMapping("/getAllRestaurantPurchases/{restaurantOwnerId}")
    public List<RestaurantPurchase> getAllRestaurantPurchasesByRestaurantOwnerId(@PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String sql1 = "SELECT * FROM ManagedBy M, Purchase P, RestaurantOwner R WHERE R.user_id = M.restaurant_owner_id AND P.restaurant_id = M.restaurant_id AND P.is_paid = 1 AND P.being_prepared = 1 AND R.user_id = ?;";
        List<Purchase> list = jdbcTemplate.query(sql1, new PurchaseMapper(),restaurantOwnerId );
        List<RestaurantPurchase> restaurantPurchaseList = new ArrayList<>();

        for (Purchase purchase : list) {
            RestaurantPurchase restaurantPurchase = new RestaurantPurchase();
            restaurantPurchase.setPurchase(purchase);

            String sql2 = "SELECT * FROM Restaurant R WHERE R.restaurant_id = ?;";
            Restaurant restaurant = jdbcTemplate.queryForObject(sql2, new RestaurantMapper(), purchase.getRestaurant_id());
            restaurantPurchase.setRestaurant(restaurant);

            String sql3 = "SELECT * FROM Address A WHERE A.address_id = ?;";
            Address address = jdbcTemplate.queryForObject(sql3, new AddressMapper(), purchase.getAddress_id());
            restaurantPurchase.setAddress(address);

            String sql4 = "SELECT * FROM Customer R, User U WHERE U.user_id = R.user_id AND U.user_id = ?;";
            Customer customer = jdbcTemplate.queryForObject(sql4, new CustomerMapper(), purchase.getCustomer_id());
            restaurantPurchase.setCustomer(customer);

            String sql = "SELECT * FROM FoodInPurchase FP WHERE FP.purchase_id = ?;";
            List<FoodInPurchase> foodList = jdbcTemplate.query(sql, new FoodInPurchaseMapper(), purchase.getPurchase_id());
            List<PurchaseItem> purchaseItemList = new ArrayList<>();

            for (FoodInPurchase foodInPurchase: foodList) {
                int food_id = foodInPurchase.getFood_id();
                int food_order = foodInPurchase.getFood_order();

                String foodSql = "SELECT * FROM Food F WHERE F.food_id = ?;";
                Food food = jdbcTemplate.queryForObject(foodSql, new FoodMapper(), food_id);

                String ingredientSql = "SELECT * FROM Ingredient I WHERE I.food_id = ? AND I.ingredient_id IN (SELECT IP.ingredient_id FROM IngredientInPurchase IP WHERE IP.purchase_id = ? AND IP.food_order = ?);";
                List<Ingredient> ingredientList = jdbcTemplate.query(ingredientSql, new IngredientMapper(), food_id, purchase.getPurchase_id(), food_order);

                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setFood(food);
                purchaseItem.setIngredientList(ingredientList);
                purchaseItem.setFood_order(food_order);
                purchaseItemList.add(purchaseItem);
            }
            restaurantPurchase.setPurchaseItemList(purchaseItemList);
            restaurantPurchaseList.add(restaurantPurchase);
        }

        return restaurantPurchaseList;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRestaurantOwner(@RequestBody RestaurantOwner restaurantOwner) {
        String sql = "INSERT INTO User(username, hashed_password, password_salt, email, phone_number, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        HashPasswordHelper hashPasswordHelper = HashPasswordHelper.getInstance();
        hashPasswordHelper.setPassword(restaurantOwner.getPassword_salt());
        String hashed_pass = hashPasswordHelper.Hash();
        jdbcTemplate.update(sql, restaurantOwner.getUsername(), hashed_pass, restaurantOwner.getPassword_salt(),
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
        String checkSql = "SELECT EXISTS (SELECT * FROM RestaurantOwner R WHERE R.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, restaurantOwnerId);

        if (!exists) { // if restaurant owner does not exist
            return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        SimpleJdbcInsert insertIntoRestaurant = new SimpleJdbcInsert(jdbcTemplate).withTableName("Restaurant").usingColumns("restaurant_name", "district",
                "open_hour", "close_hour", "min_delivery_price", "is_top_restaurant", "coupon_limit", "discount_percentage").usingGeneratedKeyColumns("restaurant_id");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("restaurant_name", restaurant.getRestaurant_name());
        parameters.put("district", restaurant.getDistrict());
        parameters.put("open_hour", restaurant.getOpen_hour());
        parameters.put("close_hour", restaurant.getClose_hour());
        parameters.put("min_delivery_price", restaurant.getMin_delivery_price());
        parameters.put("is_top_restaurant", Boolean.FALSE);
        parameters.put("coupon_limit", restaurant.getCoupon_limit());
        parameters.put("discount_percentage", restaurant.getDiscount_percentage());

        // Get the inserted id back
        int restaurantId = insertIntoRestaurant.executeAndReturnKey(parameters).intValue();

        String sql = "INSERT INTO ManagedBy(restaurant_id, restaurant_owner_id) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, restaurantId, restaurantOwnerId); // insert to ManagedBy table

        return new ResponseEntity<>("Restaurant Has Been Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/addRestaurantOwner/{restaurantId}/{restaurantOwnerId}")
    public ResponseEntity<String> addRestaurantOwnerToRestaurant(@PathVariable("restaurantId") int restaurantId,
                                                                 @PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, restaurantId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM RestaurantOwner R WHERE R.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, restaurantOwnerId);

        if (!exists1) { // if restaurant owner does not exist
            return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM ManagedBy M WHERE M.restaurant_id = ? AND M.restaurant_owner_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, restaurantId, restaurantOwnerId);

        if (exists2) { // if restaurant owner, restaurant pair already exists
            return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " already manages Restaurant With ID: " + restaurantId + "!", HttpStatus.BAD_REQUEST);
        }

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

    @DeleteMapping("/deleteRestaurantOwner/{restaurantId}/{restaurantOwnerId}")
    public ResponseEntity<String> deleteRestaurantOwnerFromRestaurant(@PathVariable("restaurantId") int restaurantId,
                                                                 @PathVariable("restaurantOwnerId") int restaurantOwnerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, restaurantId);

        if (!exists) { // if restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM RestaurantOwner R WHERE R.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, restaurantOwnerId);

        if (!exists1) { // if restaurant owner does not exist
            return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM ManagedBy M WHERE M.restaurant_id = ? AND M.restaurant_owner_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, restaurantId, restaurantOwnerId);

        if (!exists2) { // if restaurant owner, restaurant pair does not exist
            return new ResponseEntity<>("Restaurant Owner With ID: " + restaurantOwnerId + " already does not manage Restaurant With ID: " + restaurantId + "!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM ManagedBy M WHERE M.restaurant_id = ? AND M.restaurant_owner_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, restaurantId, restaurantOwnerId); // delete from the ManagedBy table

        return new ResponseEntity<>("Restaurant Owner Is Successfully Removed From The Restaurant!", HttpStatus.OK);
    }
}
