package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.DiscountCouponMapper;
import com.micra.GOtur.mappers.FoodCategoryMapper;
import com.micra.GOtur.models.DiscountCoupon;
import com.micra.GOtur.models.FoodCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/discountCoupon")
@CrossOrigin
public class DiscountCouponController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DiscountCouponController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<DiscountCoupon> getAllDiscountCoupons() {

        String sql = "SELECT * FROM DiscountCoupon D;";
        List<DiscountCoupon> allDiscountCoupons = jdbcTemplate.query(sql, new DiscountCouponMapper());
        return allDiscountCoupons;
    }

    @GetMapping("/{discountCouponId}")
    public DiscountCoupon getDiscountCoupon(@PathVariable("discountCouponId") int discountCouponId) {
        String sql = "SELECT * FROM DiscountCoupon D WHERE d.coupon_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new DiscountCouponMapper(), discountCouponId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getAll/{customerId}")
    public List<DiscountCoupon> getAllDiscountCoupons(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM DiscountCoupon D WHERE D.coupon_owner_id = ?;";

        return jdbcTemplate.query(sql, new DiscountCouponMapper(), customerId);
    }

    @GetMapping("/getAllActive/{customerId}")
    public List<DiscountCoupon> getAllActiveDiscountCoupons(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM DiscountCoupon D WHERE D.coupon_owner_id = ? AND D.is_used = 0;";

        return jdbcTemplate.query(sql, new DiscountCouponMapper(), customerId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDiscountCoupon(
            @RequestBody DiscountCoupon discountCoupon
    ) {

        String checkCustomer = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean existsCustomer = jdbcTemplate.queryForObject(checkCustomer, Boolean.class, discountCoupon.getCoupon_owner_id());
        if (!existsCustomer) {
            return new ResponseEntity<>("Customer with id: " + discountCoupon.getCoupon_owner_id() + " does not exist! Failed!", HttpStatus.BAD_REQUEST);
        }

        String checkRestaurant = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean existsRestaurant = jdbcTemplate.queryForObject(checkRestaurant, Boolean.class, discountCoupon.getRestaurant_id());
        if (!existsRestaurant) {
            return new ResponseEntity<>("Restaurant with id: " + discountCoupon.getRestaurant_id() + " does not exist! Failed!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO DiscountCoupon(coupon_owner_id, restaurant_id, is_used) VALUES (?,?,?);";
        jdbcTemplate.update(sql, discountCoupon.getCoupon_owner_id(), discountCoupon.getRestaurant_id(), Boolean.FALSE);

        return new ResponseEntity<>("Discount coupon added for Customer with id: " +
                discountCoupon.getCoupon_owner_id(), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{discountCouponId}")
    public ResponseEntity<String> deleteDiscountCoupon(
            @PathVariable("discountCouponId") int discountCouponId
    ) {
        String sql = "DELETE FROM DiscountCoupon D WHERE D.coupon_id = (?);";
        jdbcTemplate.update(sql, discountCouponId);

        return new ResponseEntity<>("Discount Coupon with id: " + discountCouponId +
                " is successfully deleted!", HttpStatus.OK);
    }

}
