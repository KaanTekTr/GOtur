package com.micra.GOtur.controllers;

import com.micra.GOtur.helpers.HashPasswordHelper;
import com.micra.GOtur.mappers.CustomerMapper;
import com.micra.GOtur.mappers.RestaurantOwnerMapper;
import com.micra.GOtur.mappers.TokenMapper;
import com.micra.GOtur.models.Customer;
import com.micra.GOtur.models.RestaurantOwner;
import com.micra.GOtur.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private  HashPasswordHelper hashPasswordHelper = HashPasswordHelper.getInstance();
    public ProfileController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login/customer")
    public ResponseEntity<String> loginCustomer(@RequestParam String email, @RequestParam String password ) {
        String checkUser = "SELECT EXISTS (SELECT *  FROM Customer R, User U WHERE U.user_id = R.user_id AND U.email = ?);";
        boolean existsUser = jdbcTemplate.queryForObject(checkUser, Boolean.class, email);
        if (!existsUser) {
            return new ResponseEntity<>("User with email: " + email + " does not exist!", HttpStatus.BAD_REQUEST);
        }
        hashPasswordHelper = HashPasswordHelper.getInstance();
        hashPasswordHelper.setPassword(password);
        String hashedPassword = hashPasswordHelper.Hash();
        String userSQL = "SELECT *  FROM Customer R, User U WHERE U.user_id = R.user_id AND U.email = ?;";

        Customer customer = jdbcTemplate.queryForObject(userSQL, new CustomerMapper(), email);
        if (customer.getHashed_password().equals(hashedPassword)) {
            Token token = new Token();
            String sqlToken = "INSERT INTO Token( user_id, token, is_actively_used, last_active) VALUES (?,?,?,?);";
            jdbcTemplate.update(sqlToken, customer.getUser_id(), token.generateToken(), true, LocalDateTime.now());
            return new ResponseEntity<>(""+ customer.getUser_id(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Incorrect login credentials.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login/restaurantOwner")
    public ResponseEntity<String> loginRestaurantOwner(@RequestParam String email, @RequestParam String password ) {
        String checkUser = "SELECT EXISTS (SELECT *  FROM RestaurantOwner R, User U WHERE U.user_id = R.user_id AND U.email = ?);";
        boolean existsUser = jdbcTemplate.queryForObject(checkUser, Boolean.class, email);
        if (!existsUser) {
            return new ResponseEntity<>("User with email: " + email + " does not exist!", HttpStatus.BAD_REQUEST);
        }
        hashPasswordHelper = HashPasswordHelper.getInstance();
        hashPasswordHelper.setPassword(password);
        String hashedPassword = hashPasswordHelper.Hash();
        String userSQL = "SELECT *  FROM RestaurantOwner R, User U WHERE U.user_id = R.user_id AND U.email = ?;";

        RestaurantOwner ro = jdbcTemplate.queryForObject(userSQL, new RestaurantOwnerMapper(), email);

        if (hashedPassword.equals(ro.getHashed_password())) {
            Token token = new Token();
            String sqlToken = "INSERT INTO Token( user_id, token, is_actively_used, last_active) VALUES (?,?,?,?);";
            jdbcTemplate.update(sqlToken,ro.getUser_id(), token.generateToken(), true, LocalDateTime.now());
            return new ResponseEntity<>(""+ ro.getUser_id(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Incorrect login credentials.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logout/customer/{customerId}")
    public ResponseEntity<String> logOutCustomer(@PathVariable("customerId") int customerId) {
        String sql = "SELECT T.token_id, T.user_id, T.token, T.is_actively_used, T.last_active FROM Customer C, User U, Token T WHERE U.user_id = C.user_id AND U.user_id = T.user_id AND U.user_id = ?;";
        Token token = jdbcTemplate.queryForObject(sql, new TokenMapper(), customerId );

        if ( token.is_actively_used() ) {
            String logoutsql = "UPDATE Token T SET T.last_active = ?, T.is_actively_used = ? WHERE T.token_id = ?;";
            jdbcTemplate.update(logoutsql, LocalDateTime.now(), false, token.getToken_id());
            String deleteSql = "DELETE FROM Token T WHERE T.token_id = ?;";
            jdbcTemplate.update(deleteSql, token.getToken_id()); // delete the token

            return new ResponseEntity<>("Log out successful", HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("The customer with id  "+ customerId + " is not logged in.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout/restaurantOwner/{restaurantOwnerId}")
    public ResponseEntity<String> logOutRestaurantOwner(@PathVariable("restaurantOwnerId") int restaurantOwnerID) {
        String sql = "SELECT T.token_id, T.user_id, T.token, T.is_actively_used, T.last_active FROM RestaurantOwner R, User U, Token T WHERE U.user_id = R.user_id AND U.user_id = T.user_id AND U.user_id = ?;";
        Token token = jdbcTemplate.queryForObject(sql, new TokenMapper(), restaurantOwnerID);

        if (token.is_actively_used()) {
            String logoutsql = "UPDATE Token T SET T.last_active = ?, T.is_actively_used = ? WHERE T.token_id = ?;";
            jdbcTemplate.update(logoutsql, LocalDateTime.now(), false, token.getToken_id());
            String deleteSql = "DELETE FROM Token T WHERE T.token_id = ?;";
            jdbcTemplate.update(deleteSql, token.getToken_id()); // delete the toke
            return new ResponseEntity<>("Log out successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The Restaurant Owner with id  " + restaurantOwnerID + " is not logged in.", HttpStatus.BAD_REQUEST);
        }
    }


}
