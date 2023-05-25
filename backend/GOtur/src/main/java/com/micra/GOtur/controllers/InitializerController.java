package com.micra.GOtur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/initialize")
@CrossOrigin
public class InitializerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public InitializerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/initializeDB")
    public ResponseEntity<String> createDB() {
        jdbcTemplate.execute("DROP DATABASE IF EXISTS gotur");
        jdbcTemplate.execute("CREATE DATABASE gotur CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

        return new ResponseEntity<>("Successfully Initialized The Database", HttpStatus.OK);
    }
}
