package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.CustomerMapper;
import com.micra.GOtur.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CustomerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM Customer R, User U WHERE U.user_id = R.user_id;";

        List<Customer> list = jdbcTemplate.query(sql, new CustomerMapper());

        return list;
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM Customer R, User U WHERE U.user_id = R.user_id AND U.user_id = ?;";

        return jdbcTemplate.queryForObject(sql, new CustomerMapper(), customerId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        String sql = "INSERT INTO User(username, hashed_password, password_salt, email, phone_number, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, customer.getUsername(), customer.getHashed_password(), customer.getPassword_salt(),
                customer.getEmail(), customer.getPhone_number(), customer.getAge(), customer.getGender());

        int insertedId = getIdByEmail(customer.getEmail());

        String sql2 = "INSERT INTO Customer(user_id, payment_method) VALUES (?, ?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, insertedId, customer.getPayment_method());

        return new ResponseEntity<>("Customer Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/addFriends/{customerId1}/{customerId2}")
    public ResponseEntity<String> addFriends(@PathVariable("customerId1") int customerId1,
                                             @PathVariable("customerId2") int customerId2) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Friend F WHERE (F.customer1_id = ? AND F.customer2_id = ?) OR (F.customer1_id = ? AND F.customer2_id = ?));";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId1, customerId2, customerId2, customerId1);

        if (exists) {
            return new ResponseEntity<>("The friendship already exists!", HttpStatus.BAD_REQUEST);
        }

        String sql2 = "INSERT INTO Friend(customer1_id, customer2_id) VALUES (?, ?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, customerId1, customerId2);

        return new ResponseEntity<>("Friendship successfully added!", HttpStatus.OK);
    }

    public int getIdByEmail(String email) {
        String sql = "SELECT user_id FROM USER U WHERE U.email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") int customerId) {
        String sql = "DELETE FROM USER U WHERE U.user_id = ?;";
        jdbcTemplate.update(sql, customerId);

        return new ResponseEntity<>("Customer With ID: " + customerId + " has been deleted!",
                HttpStatus.OK);
    }

    @DeleteMapping("/deleteFriends/{customerId1}/{customerId2}")
    public ResponseEntity<String> deleteFriends(@PathVariable("customerId1") int customerId1,
                                             @PathVariable("customerId2") int customerId2) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Friend F WHERE (F.customer1_id = ? AND F.customer2_id = ?) OR (F.customer1_id = ? AND F.customer2_id = ?));";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId1, customerId2, customerId2, customerId1);

        if (!exists) {
            return new ResponseEntity<>("The friendship does not exists!", HttpStatus.BAD_REQUEST);
        }

        String sql2 = "DELETE FROM Friend F WHERE (F.customer1_id = ? AND F.customer2_id = ?) OR (F.customer1_id = ? AND F.customer2_id = ?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, customerId1, customerId2, customerId2, customerId1);

        return new ResponseEntity<>("Friendship is successfully deleted!", HttpStatus.OK);
    }
}
