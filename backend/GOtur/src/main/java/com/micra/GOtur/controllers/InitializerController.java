package com.micra.GOtur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
        jdbcTemplate.execute("USE gotur;");

        initializeTables();

        return new ResponseEntity<>("Successfully Initialized The Database", HttpStatus.OK);
    }

    public void initializeTables() throws DataAccessException {
        String[] tableNames = new String[]{"User, Customer"};
        String[] tables = new String[]{"CREATE TABLE User(\n" +
                "    user_id int NOT NULL AUTO_INCREMENT,\n" +
                "    username varchar(255) NOT NULL UNIQUE,\n" +
                "    hashed_password varchar(255) NOT NULL,\n" +
                "    password_salt varchar(32) NOT NULL,\n" +
                "    email varchar(255) NOT NULL,\n" +
                "    phone_number varchar(255) NOT NULL UNIQUE,\n" +
                "    age int NOT NULL, \n" +
                "    gender varchar(255) NOT NULL,\n" +
                "    PRIMARY KEY (user_id));",
                "CREATE TABLE Customer(\n" +
                        "    user_id int,\n" +
                        "    balance float DEFAULT 0,\n" +
                        "    total_points float DEFAULT 0,\n" +
                        "    payment_method varchar(255),\n" +
                        "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (user_id));"};

        for (String curQuery : tables) {
            System.out.println(">>" + curQuery);
            jdbcTemplate.execute(curQuery);
        }
    }
}
