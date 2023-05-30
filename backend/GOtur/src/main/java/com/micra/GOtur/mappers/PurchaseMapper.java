package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Purchase;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PurchaseMapper implements RowMapper<Purchase> {
    @Override
    public Purchase mapRow(ResultSet rs, int rowNum) throws SQLException {
        int purchase_id = rs.getInt("purchase_id");
        int customer_id = rs.getInt("customer_id");
        int address_id = rs.getInt("address_id");
        int restaurant_id = rs.getInt("restaurant_id");
        String customer_note = rs.getString("customer_note");
        Boolean is_paid = rs.getBoolean("is_paid");
        Boolean is_group_purchase = rs.getBoolean("is_group_purchase");
        Boolean being_prepared = rs.getBoolean("being_prepared");
        Boolean is_departed = rs.getBoolean("is_departed");
        Boolean is_delivered = rs.getBoolean("is_delivered");
        Boolean is_canceled = rs.getBoolean("is_canceled");
        float total_price = rs.getFloat("total_price");
        LocalDate purchase_time = null;
        if ( rs.getDate("purchase_time") != null ) {
            purchase_time = rs.getDate("purchase_time").toLocalDate();
        }

        Purchase purchase = new Purchase();
        purchase.setPurchase_id(purchase_id);
        purchase.setCustomer_id(customer_id);
        purchase.setAddress_id(address_id);
        purchase.setRestaurant_id(restaurant_id);
        purchase.setCustomer_note(customer_note);
        purchase.setIs_paid(is_paid);
        purchase.setIs_group_purchase(is_group_purchase);
        purchase.setBeing_prepared(being_prepared);
        purchase.setIs_departed(is_departed);
        purchase.setIs_delivered(is_delivered);
        purchase.setIs_canceled(is_canceled);
        purchase.setPurchase_time(purchase_time);
        purchase.setTotal_price(total_price);

        return purchase;
    }
}
