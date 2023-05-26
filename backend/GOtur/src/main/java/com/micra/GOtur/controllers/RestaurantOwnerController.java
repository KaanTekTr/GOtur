package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.RestaurantOwnerMapper;
import com.micra.GOtur.models.RestaurantOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public int getIdByEmail(String email) {
        String sql = "SELECT user_id FROM USER U WHERE U.email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }
}
