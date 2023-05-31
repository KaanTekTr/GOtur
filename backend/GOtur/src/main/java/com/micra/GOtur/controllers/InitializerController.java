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
        initializeViews();
        //initializeConstraints();

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
                        "    coupon_limit float DEFAULT 0,\n" +
                        "    discount_percentage float DEFAULT 5,\n" +
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
                        "    total_price float DEFAULT 0,\n" +
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
                        "    user_id int NOT NULL,\n" +
                        "    income int DEFAULT 0,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (user_id));",
                "CREATE TABLE Promote(\n" +
                        "    promotion_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    promoter_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    promotion_code varchar(30),\n" +
                        "    profit_rate int,\n" +
                        "    expiration_date date,\n" +
                        "    user_quota int,\n" +
                        "    FOREIGN KEY (promoter_id) REFERENCES Promoter(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (promotion_id));",
                "CREATE TABLE DiscountCoupon(\n" +
                        "    coupon_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    coupon_owner_id int NOT NULL,\n" +
                        "    restaurant_id int NOT NULL,\n" +
                        "    is_used boolean,\n" +
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
                        "    food_order int NOT NULL,\n" +
                        "    FOREIGN KEY (food_id) REFERENCES Food(food_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (food_id, purchase_id, food_order));",
                "CREATE TABLE IngredientInPurchase(\n" +
                        "    ingredient_id int NOT NULL,\n" +
                        "    purchase_id int NOT NULL,\n" +
                        "    food_order int NOT NULL,\n" +
                        "    FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (purchase_id) REFERENCES Purchase(purchase_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (ingredient_id, purchase_id, food_order));",
                "CREATE TABLE Token(\n" +
                        "    token_id int NOT NULL AUTO_INCREMENT,\n" +
                        "    user_id int NOT NULL,\n"+
                        "    token varchar(250),\n" +
                        "    is_actively_used boolean,\n" +
                        "    last_active date,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                        "    PRIMARY KEY (token_id));"};

        for (String curQuery : tables) {
            System.out.println(">>" + curQuery);
            jdbcTemplate.execute(curQuery);
        }
    }

    public void initializeTriggers() throws DataAccessException {
        String[] triggerNames = new String[]{"increase_restaurant_count", "decrease_restaurant_count", "update_restaurant_rating", "update_restaurant_rating2", "update_purchase_total_after_food",
                "update_purchase_total_after_ingredient", "update_purchase_total_before_delete_food", "update_purchase_total_before_delete_ingredient",
                "admin_report_count"};
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
                        "    DECLARE size float;\n" +
                        "    DECLARE total float;\n" +
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
                        "    SET R.rating = total / size,\n" +
                        "    R.is_top_restaurant = CASE \n" +
                        "        WHEN R.rating < 4.5 THEN 0\n" +
                        "        ELSE 1\n" +
                        "    END \n" +
                        "    WHERE R.restaurant_id = updated_restaurant_id;\n" +
                        "END;",
                "CREATE TRIGGER update_restaurant_rating2\n" +
                        "BEFORE DELETE ON Review\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    DECLARE size float;\n" +
                        "    DECLARE total float;\n" +
                        "    DECLARE updated_restaurant_id INT;\n" +
                        "\n" +
                        "    SELECT Purchase.restaurant_id\n" +
                        "    INTO updated_restaurant_id\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Review.review_id = OLD.review_id;\n" +
                        "\n" +
                        "    SELECT COUNT(*)\n" +
                        "    INTO size\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Purchase.restaurant_id = updated_restaurant_id AND Review.review_id != OLD.review_id;\n" +
                        "\n" +
                        "    SELECT SUM(Review.rate)\n" +
                        "    INTO total\n" +
                        "    FROM Review NATURAL JOIN Purchase\n" +
                        "    WHERE Purchase.restaurant_id = updated_restaurant_id AND Review.review_id != OLD.review_id;\n" +
                        "\n" +
                        "    UPDATE Restaurant R\n" +
                        "    SET R.rating = CASE \n" +
                        "        WHEN size = 0 THEN 0\n" +
                        "        ELSE total / size\n" +
                        "    END, \n" +
                        "    R.is_top_restaurant = CASE \n" +
                        "        WHEN R.rating < 4.5 THEN 0\n" +
                        "        ELSE 1\n" +
                        "    END \n" +
                        "    WHERE R.restaurant_id = updated_restaurant_id;\n" +
                        "END;",
                "CREATE TRIGGER update_purchase_total_after_food\n" +
                        "AFTER INSERT ON FoodInPurchase\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE Purchase P\n" +
                        "    SET P.total_price = P.total_price + (SELECT F.price FROM Food F WHERE F.food_id = NEW.food_id)\n" +
                        "    WHERE P.purchase_id = NEW.purchase_id;\n" +
                        "END;",
                "CREATE TRIGGER update_purchase_total_after_ingredient\n" +
                        "AFTER INSERT ON IngredientInPurchase\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE Purchase P\n" +
                        "    SET P.total_price = P.total_price + (SELECT I.price FROM Ingredient I WHERE I.ingredient_id = NEW.ingredient_id)\n" +
                        "    WHERE P.purchase_id = NEW.purchase_id;\n" +
                        "END;",
                "CREATE TRIGGER update_purchase_total_before_delete_food\n" +
                        "BEFORE DELETE ON FoodInPurchase\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE Purchase P\n" +
                        "    SET P.total_price = P.total_price - (SELECT F.price FROM Food F WHERE F.food_id = OLD.food_id)\n" +
                        "    WHERE P.purchase_id = OLD.purchase_id;\n" +
                        "END;",
                "CREATE TRIGGER update_purchase_total_before_delete_ingredient\n" +
                        "BEFORE DELETE ON IngredientInPurchase\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE Purchase P\n" +
                        "    SET P.total_price = P.total_price - (SELECT I.price FROM Ingredient I WHERE I.ingredient_id = OLD.ingredient_id)\n" +
                        "    WHERE P.purchase_id = OLD.purchase_id;\n" +
                        "END;",
                "CREATE TRIGGER admin_report_count\n" +
                        "AFTER INSERT ON Report\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "    UPDATE Admin\n" +
                        "    SET report_count = (SELECT count(*)\n" +
                        "                        FROM Report\n" +
                        "                        WHERE admin_id = NEW.admin_id)\n" +
                        "    WHERE user_id = NEW.admin_id;\n" +
                        "END;"
        };

        for (String curTrigger : triggerNames) {
            jdbcTemplate.execute("DROP TRIGGER IF EXISTS " + curTrigger);
        }

        for (String curTrigger : triggers) {
            System.out.println(">>" + curTrigger);
            jdbcTemplate.execute(curTrigger);
        }
    }
    public void initializeViews() throws DataAccessException {
        String[] viewNames = new String[]{"restaurantsForCustomer", "promotersForCustomer"};
        String[] views = new String[]{
                "CREATE VIEW Restaurants_For_Customer AS " +
                        "SELECT R.restaurant_id, R.restaurant_name, R.district, R.open_hour, " +
                        "R.close_hour, " +
                        "R.min_delivery_price, R.is_top_restaurant, " +
                        "R.rating " +
                        "FROM Address A,  Customer C, Restaurant R " +
                        "WHERE C.user_id = A.customer_id AND " +
                        "      A.district = R.district;",

                "CREATE VIEW Promoters_For_Customers AS " +
                        "SELECT U.username, U.age, U.gender, U.email " +
                        "FROM Promoter P natural join User U " +
                        "WHERE P.user_id = U.user_id;"
        };

        for (String curView : viewNames) {
            jdbcTemplate.execute("DROP VIEW IF EXISTS " + curView);
        }

        for (String curView : views) {
            System.out.println(">>" + curView);
            jdbcTemplate.execute(curView);
        }
    }

    /*public void initializeConstraints() throws DataAccessException {
        String[] constraintNames = new String[]{"duplicate_review", "check_single_purchase", "purchase_total_price_check"};
        String[] constraints = new String[]{
                "CREATE ASSERTION duplicate_review " +
                        "CHECK( NOT EXISTS (SELECT R1.review_id, R2.review_id FROM Review R1, Review R2, Purchase O " +
                        "WHERE R1.review_id != R2.review_id AND R1.purchase_id = O.purchase_id AND R2.purchase_id = O.purchase_id));",

                "CREATE ASSERTION check_single_purchase " +
                        "CHECK(NOT EXISTS (SELECT O1.purchase_id, O2.purchase_id FROM Purchase O1, Purchase O2, Customer C " +
                        "WHERE O1.purchase_id != O2.purchase_id AND O1.customer_id = C.user_id AND O2.customer_id = C.user_id " +
                        "AND O1.is_paid = 0 AND O2.is_paid = 0 AND O1.is_group_purchase = 0 AND NOT O2.is_group_purchase = 0));",

                "CREATE ASSERTION purchase_total_price_check " +
                        "CHECK NOT EXISTS ( " +
                        "SELECT * " +
                        "FROM Purchase o " +
                        "JOIN Restaurant r ON o.restaurant_id = r.restaurant_id " +
                        "WHERE o.total_price < r.min_delivery_price " +
                        " );"
        };

        for (String curConstraint : constraintNames) {
            jdbcTemplate.execute("DROP ASSERTION IF EXISTS " + curConstraint);
        }

        for (String curConstraint : constraints) {
            System.out.println(">>" + curConstraint);
            jdbcTemplate.execute(curConstraint);
        }
    }*/
}
