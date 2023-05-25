package com.micra.GOtur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReviewController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<String> createReview(
            @PathVariable("id") int id,
            @RequestBody String name) {

        return new ResponseEntity<>("Id is " + id + " name is " + name , HttpStatus.OK);

    }
}
