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
}
