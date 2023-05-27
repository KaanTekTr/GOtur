package com.micra.GOtur.mappers;

import com.micra.GOtur.models.DiscountCoupon;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DiscountCouponMapper implements RowMapper<DiscountCoupon> {

    @Override
    public DiscountCoupon mapRow(ResultSet rs, int rowNum) throws SQLException {
        int coupon_id = rs.getInt("coupon_id");
        int coupon_owner_id = rs.getInt("coupon_owner_id");
        int restaurant_id = rs.getInt("restaurant_id");
        int discount_percentage = rs.getInt("discount_percentage");
        LocalDate expiration_date = rs.getDate("expiration_date").toLocalDate();

        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setCoupon_id(coupon_id);
        discountCoupon.setCoupon_owner_id(coupon_owner_id);
        discountCoupon.setRestaurant_id(restaurant_id);
        discountCoupon.setDiscount_percentage(discount_percentage);
        discountCoupon.setExpiration_date(expiration_date);

        return discountCoupon;
    }

}
