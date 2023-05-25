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
                        "    PRIMARY KEY (user_id));",
                "CREATE TABLE Friend(\n" +
                        "    customer1_id int,\n" +
                        "    customer2_id int,\n" +
                        "    FOREIGN KEY (customer1_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (customer2_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (customer1_id, customer2_id));",
                "CREATE TABLE Address(\n" +
                        "    address_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    customer_id int,\n" +
                        "    is_primary boolean,\n" +
                        "    city varchar(255),\n" +
                        "    district varchar(255),\n" +
                        "    street_num varchar(255),\n" +
                        "    street_name varchar(255),\n" +
                        "    building_num varchar(255),\n" +
                        "    detailed_desc text,\n" +
                        "    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (address_id));",
                "CREATE TABLE Restaurant(\n" +
                        "    restaurant_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    restaurant_name varchar(255),\n" +
                        "    district varchar(255),\n" +
                        "    total_earnings float DEFAULT 0,\n" +
                        "    open_hour varchar(255),\n" +
                        "    close_hour varchar(255),\n" +
                        "    min_delivery_price int,\n" +
                        "    is_top_restaurant boolean,\n" +
                        "    rating float,\n" +
                        "    PRIMARY KEY (restaurant_id));",
                "CREATE TABLE Shopping(\n" +
                        "    shopping_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    customer_id int,\n" +
                        "    address_id int,\n" +
                        "    restaurant_id int,\n" +
                        "    customer_note varchar(255),\n" +
                        "    is_paid boolean,\n" +
                        "    is_group_order boolean,\n" +
                        "    being_prepared boolean,\n" +
                        "    is_departed boolean,\n" +
                        "    is_delivered boolean,\n" +
                        "    is_canceled boolean,\n" +
                        "    order_time datetime,\n" +
                        "    total_price float,\n" +
                        "    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (address_id) REFERENCES Address(address_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (shopping_id));"};

        for (String curQuery : tables) {
            System.out.println(">>" + curQuery);
            jdbcTemplate.execute(curQuery);
        }
    }
}
