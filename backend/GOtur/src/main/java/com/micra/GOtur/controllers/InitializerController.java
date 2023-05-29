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
        initializeTriggers();

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
                        "    address_name varchar(255),\n" +
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
                        "    rating float DEFAULT 0,\n" +
                        "    PRIMARY KEY (restaurant_id));",
                "CREATE TABLE Purchase(\n" +
                        "    purchase_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    customer_id int,\n" +
                        "    address_id int,\n" +
                        "    restaurant_id int,\n" +
                        "    customer_note varchar(255),\n" +
                        "    is_paid boolean DEFAULT 0,\n" +
                        "    is_group_purchase boolean,\n" +
                        "    being_prepared boolean DEFAULT 0,\n" +
                        "    is_departed boolean DEFAULT 0,\n" +
                        "    is_delivered boolean DEFAULT 0,\n" +
                        "    is_canceled boolean DEFAULT 0,\n" +
                        "    purchase_time datetime,\n" +
                        "    total_price float,\n" +
                        "    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (address_id) REFERENCES Address(address_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (purchase_id));",
                "CREATE TABLE Review(\n" +
                        "    review_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    purchase_id int NOT NULL,\n" +
                        "    reviewer_id int NOT NULL,\n" +
                        "    comment varchar(255),\n" +
                        "    rate float,\n" +
                        "    review_date datetime,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (reviewer_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (review_id));",
                "CREATE TABLE PurchaseGroup(\n" +
                        "    group_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    group_owner_id int NOT NULL,\n" +
                        "    group_name varchar(255),\n" +
                        "    group_balance float DEFAULT 0,\n" +
                        "    FOREIGN KEY (group_owner_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (group_id));",
                "CREATE TABLE PurchaseInGroup(\n" +
                        "    purchase_id int NOT NULL,\n" +
                        "    group_id int NOT NULL,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (group_id) REFERENCES PurchaseGroup(group_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (purchase_id, group_id));",
                "CREATE TABLE Forms(\n" +
                        "    group_id int NOT NULL,\n" +
                        "    group_member_id int NOT NULL,\n" +
                        "    FOREIGN KEY (group_id) REFERENCES PurchaseGroup(group_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (group_member_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (group_id, group_member_id));",
                "CREATE TABLE Admin(\n" +
                        "    user_id int NOT NULL,\n" +
                        "    report_count int DEFAULT 0,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (user_id));",
                "CREATE TABLE Report(\n" +
                        "    report_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    admin_id int NOT NULL,\n" +
                        "    details text,\n" +
                        "    report_type varchar(255),\n" +
                        "    report_date datetime,\n" +
                        "    FOREIGN KEY (admin_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (report_id));",
                "CREATE TABLE Analyzes(\n" +
                        "    report_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    FOREIGN KEY (report_id) REFERENCES Report(report_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (report_id, restaurant_id));",
                "CREATE TABLE Favorites(\n" +
                        "    customer_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (customer_id, restaurant_id));",
                "CREATE TABLE RestaurantOwner(\n" +
                        "    user_id int NOT NULL,\n" +
                        "    restaurant_count int DEFAULT 0,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (user_id));",
                "CREATE TABLE ManagedBy(\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    restaurant_owner_id int NOT NULL,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_owner_id) REFERENCES RestaurantOwner(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (restaurant_id, restaurant_owner_id));",
                "CREATE TABLE Promoter(\n" +
                        "    promoter_id int NOT NULL,\n" +
                        "    income int,\n" +
                        "    FOREIGN KEY (promoter_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (promoter_id));",
                "CREATE TABLE Promote(\n" +
                        "    promotion_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    promoter_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    promotion_code varchar(30),\n" +
                        "    profit_rate int,\n" +
                        "    expiration_date date,\n" +
                        "    user_quota int,\n" +
                        "    FOREIGN KEY (promoter_id) REFERENCES Promoter(promoter_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (promotion_id));",
                "CREATE TABLE DiscountCoupon(\n" +
                        "    coupon_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    coupon_owner_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    discount_percentage int,\n" +
                        "    expiration_date date,\n" +
                        "    FOREIGN KEY (coupon_owner_id) REFERENCES Customer(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (coupon_id));",
                "CREATE TABLE FoodCategory(\n" +
                        "    food_category_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    food_category_name varchar(255),\n" +
                        "    PRIMARY KEY (food_category_id));",
                "CREATE TABLE Serves(\n" +
                        "    food_category_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    FOREIGN KEY (food_category_id) REFERENCES FoodCategory(food_category_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (food_category_id, restaurant_id));",
                "CREATE TABLE MenuCategory(\n" +
                        "    menu_category_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    menu_category_name varchar(255),\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (menu_category_id));",
                "CREATE TABLE Food(\n" +
                        "    food_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    food_category_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    menu_category_id int NOT NULL,\n" +
                        "    food_name varchar(255),\n" +
                        "    fixed_ingredients varchar(255),\n" +
                        "    price int,\n" +
                        "    FOREIGN KEY (food_category_id) REFERENCES FoodCategory(food_category_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (menu_category_id) REFERENCES MenuCategory(menu_category_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (food_id));",
                "CREATE TABLE Ingredient(\n" +
                        "    ingredient_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    food_id int NOT NULL,\n" +
                        "    ingredient_name varchar(255),\n" +
                        "    price int,\n" +
                        "    FOREIGN KEY (food_id) REFERENCES Food(food_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (ingredient_id));",
                "CREATE TABLE FoodInPurchase(\n" +
                        "    food_id int NOT NULL,\n" +
                        "    purchase_id int NOT NULL,\n" +
                        "    FOREIGN KEY (food_id) REFERENCES Food(food_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (food_id, purchase_id));",
                "CREATE TABLE IngredientInPurchase(\n" +
                        "    ingredient_id int NOT NULL,\n" +
                        "    purchase_id int NOT NULL,\n" +
                        "    FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (ingredient_id, purchase_id));"};

        for (String curQuery : tables) {
            System.out.println(">>" + curQuery);
            jdbcTemplate.execute(curQuery);
        }
    }

    public void initializeTriggers() throws DataAccessException {
        String[] triggerNames = new String[]{"increase_restaurant_count", "decrease_restaurant_count", "update_restaurant_rating"};
        String[] triggers = new String[]{"CREATE TRIGGER increase_restaurant_count\n" +
                "AFTER INSERT ON ManagedBy\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "    UPDATE RestaurantOwner R\n" +
                "    SET restaurant_count = restaurant_count + 1\n" +
                "    WHERE R.user_id = NEW.restaurant_owner_id;\n" +
                "END;",
                "CREATE TRIGGER decrease_restaurant_count\n" +
                        "BEFORE DELETE ON Restaurant\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE RestaurantOwner R\n" +
                        "    SET restaurant_count = restaurant_count - 1\n" +
                        "    WHERE R.user_id IN (SELECT M.restaurant_owner_id FROM ManagedBy M WHERE M.restaurant_id = OLD.restaurant_id);\n" +
                        "END;",
                "CREATE TRIGGER update_restaurant_rating\n" +
                        "AFTER INSERT ON Review\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    DECLARE size INT;\n" +
                        "    DECLARE total INT;\n" +
                        "    DECLARE updated_restaurant_id INT;\n" +
                        "\n" +
                        "    SELECT Purchase.restaurant_id\n" +
                        "    INTO updated_restaurant_id\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Review.review_id = NEW.review_id;\n" +
                        "\n" +
                        "    SELECT COUNT(*)\n" +
                        "    INTO size\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Purchase.restaurant_id = updated_restaurant_id;\n" +
                        "\n" +
                        "    SELECT SUM(Review.rate)\n" +
                        "    INTO total\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Purchase.restaurant_id = updated_restaurant_id;\n" +
                        "\n" +
                        "    UPDATE Restaurant R\n" +
                        "    SET R.rating = total / size\n" +
                        "    WHERE R.restaurant_id = updated_restaurant_id;\n" +
                        "END;"};

        for (String curTrigger : triggerNames) {
            jdbcTemplate.execute("DROP TRIGGER IF EXISTS " + curTrigger);
        }

        for (String curTrigger : triggers) {
            System.out.println(">>" + curTrigger);
            jdbcTemplate.execute(curTrigger);
        }
    }
}
