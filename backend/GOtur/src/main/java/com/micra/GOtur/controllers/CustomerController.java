package com.micra.GOtur.controllers;

import com.micra.GOtur.helpers.HashPasswordHelper;
import com.micra.GOtur.mappers.AddressMapper;
import com.micra.GOtur.mappers.CustomerMapper;
import com.micra.GOtur.models.Address;
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

    @GetMapping("/allFriends/{customerId}")
    public List<Customer> getAllFriendsOfCustomer(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM Customer C, User U WHERE C.user_id = U.user_id AND C.user_id IN ((SELECT F.customer2_id FROM Friend F WHERE F.customer1_id = ?) UNION (SELECT F.customer1_id FROM Friend F WHERE F.customer2_id = ?))";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerMapper(), customerId, customerId);

        return list;
    }

    @GetMapping("/allAddress/{customerId}")
    public List<Address> getAllAddressOfCustomer(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM Address A WHERE A.customer_id = ?;";
        List<Address> list = jdbcTemplate.query(sql, new AddressMapper(), customerId);

        return list;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        String sql = "INSERT INTO User(username, hashed_password, password_salt, email, phone_number, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        HashPasswordHelper hashPasswordHelper = HashPasswordHelper.getInstance();
        hashPasswordHelper.setPassword(customer.getPassword_salt());
        String hashed_pass = hashPasswordHelper.Hash();
        jdbcTemplate.update(sql, customer.getUsername(),hashed_pass, customer.getPassword_salt(),
                customer.getEmail(), customer.getPhone_number(), customer.getAge(), customer.getGender());

        int insertedId = getIdByEmail(customer.getEmail());

        String sql2 = "INSERT INTO Customer(user_id, payment_method) VALUES (?, ?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, insertedId, customer.getPayment_method());

        return new ResponseEntity<>("Customer Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/addAddress/{customerId}")
    public ResponseEntity<String> addAddressToCustomer(@PathVariable("customerId") int customerId,
                                                       @RequestBody Address address) {
        // Check if the customer exists
        String checkSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if (!exists) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // the address is not primary
        Boolean is_primary = Boolean.FALSE;

        // Check if the customer already has an address
        String checkAddressSql = "SELECT EXISTS (SELECT * FROM Address A WHERE A.customer_id = ?);";
        boolean existsAddress = jdbcTemplate.queryForObject(checkAddressSql, Boolean.class, customerId);

        if ( !existsAddress ) { // if the customer has no address, set the new address to primary
            is_primary = Boolean.TRUE;
        }

        String sql = "INSERT INTO Address(customer_id, address_name, is_primary, city, district, street_num, street_name, building_num, detailed_desc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        System.out.println(address.getIs_primary());
        jdbcTemplate.update(sql, customerId, address.getAddress_name(), is_primary, address.getCity(), address.getDistrict(), address.getStreet_num(),
                address.getStreet_name(), address.getBuilding_num(), address.getDetailed_desc());

        return new ResponseEntity<>("Address Is Successfully Added To The Customer!", HttpStatus.OK);
    }

    @PostMapping("/setAddressPrimary/{addressId}")
    public ResponseEntity<String> setAddressAsPrimaryByAddressId(@PathVariable("addressId") int addressId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Address A WHERE A.address_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, addressId);

        if (!exists) {
            return new ResponseEntity<>("The Address With ID: " + addressId + " Does Not Exist!", HttpStatus.BAD_REQUEST);
        }

        // set the previous primary to non-primary
        String oldAddressSql = "UPDATE Address A SET A.is_primary = 0 WHERE A.is_primary = 1;";
        System.out.println(">>" + oldAddressSql);
        jdbcTemplate.update(oldAddressSql);

        // set the new address as primary
        String sql = "UPDATE Address A SET A.is_primary = ? WHERE A.address_id = ?;";
        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, Boolean.TRUE, addressId);

        return new ResponseEntity<>("Address With ID: " + addressId + " Has Been Set As Primary!", HttpStatus.OK);
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

    @PostMapping("/addFavoriteRestaurant/{customerId}/{restaurantId}")
    public ResponseEntity<String> addFavoriteRestaurantToCustomer(@PathVariable("customerId") int customerId,
                                                                  @PathVariable("restaurantId") int restaurantId) {
        // Check if the customer exists
        String checkSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if (!exists) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // check if restaurant exists
        String checkSql1 = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, restaurantId);

        if (!exists1) { // if restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // check if the restaurant is already favorited
        String checkSql2 = "SELECT EXISTS (SELECT * FROM Favorites F WHERE F.restaurant_id = ? AND F.customer_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, restaurantId, customerId);

        if (exists2) { // if restaurant, customer pair already exists
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " already favorited by Customer With ID: " + customerId + "!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Favorites(customer_id, restaurant_id) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, customerId, restaurantId);

        return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " is Successfully Favorited By The Customer With ID: " + customerId + "!", HttpStatus.OK);
    }

    @PostMapping("/addBalance/{customerId}")
    public ResponseEntity<String> addBalanceByCustomerId(@PathVariable("customerId") int customerId,
                                                         @RequestParam int transferAmount) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if (!exists) {
            return new ResponseEntity<>("The Customer With ID: " + customerId + " Does Not Exist!", HttpStatus.BAD_REQUEST);
        }

        // Update the balance of the customer
        String sql = "UPDATE Customer C SET C.balance = C.balance + ? WHERE C.user_id = ?;";
        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, transferAmount, customerId);

        return new ResponseEntity<>("The Balance Of The Customer With ID: " + customerId + " Has Been Successfully Updated!", HttpStatus.OK);
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

    @DeleteMapping("/deleteAddress/{customerId}/{addressId}")
    public ResponseEntity<String> deleteAddressFromCustomer(@PathVariable("customerId") int customerId,
                                                       @PathVariable("addressId") int addressId) {
        // Check if the customer exists
        String checkSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if (!exists) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // Check if the address exists
        String checkSql1 = "SELECT EXISTS (SELECT * FROM Address A WHERE A.address_id = ? AND A.customer_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, addressId, customerId);

        if (!exists1) { // such address does not exist!
            return new ResponseEntity<>("Address With ID: " + addressId + " does not exist in Customer With ID: " + customerId + "!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Address A WHERE A.address_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, addressId);

        return new ResponseEntity<>("Address Is Successfully Deleted From The Customer!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteFavoriteRestaurant/{customerId}/{restaurantId}")
    public ResponseEntity<String> deleteFavoriteRestaurantFromCustomer(@PathVariable("customerId") int customerId,
                                                                  @PathVariable("restaurantId") int restaurantId) {
        // Check if the customer exists
        String checkSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, customerId);

        if (!exists) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // check if restaurant exists
        String checkSql1 = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, restaurantId);

        if (!exists1) { // if restaurant does not exist
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // check if restaurant is favorited
        String checkSql2 = "SELECT EXISTS (SELECT * FROM Favorites F WHERE F.restaurant_id = ? AND F.customer_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, restaurantId, customerId);

        if (!exists2) { // if restaurant, customer pair already exists
            return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " is not favorited by Customer With ID: " + customerId + "!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Favorites F WHERE F.customer_id = ? AND F.restaurant_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, customerId, restaurantId);

        return new ResponseEntity<>("Restaurant With ID: " + restaurantId + " is Successfully Deleted From The Favorites Of The Customer With ID: " + customerId + "!", HttpStatus.OK);
    }
}
