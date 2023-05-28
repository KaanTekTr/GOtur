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
        try {
            jdbcTemplate.update(sql, restaurantId, restaurantOwnerId); // insert to ManagedBy table
        } catch (Exception e) {
            return new ResponseEntity<>("Error! Make sure restaurantId or RestaurantOwnerID is valid!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Restaurant Successfully Inserted!", HttpStatus.OK);
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
