package com.micra.GOtur.controllers;

import com.micra.GOtur.models.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PurchaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/addSinglePurchase")
    public ResponseEntity<String> addSinglePurchase(@RequestBody Purchase purchase) {
        String sql = "INSERT INTO Purchase(customer_id, address_id, restaurant_id, customer_note, is_group_purchase, total_price) VALUES (?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchase.getCustomer_id(), purchase.getAddress_id(), purchase.getRestaurant_id(),
                purchase.getCustomer_note(), Boolean.FALSE, 0f);

        return new ResponseEntity<>("Purchase Has Successfully Formed!", HttpStatus.OK);
    }
}
