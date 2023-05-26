package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DiscountCoupon {

    private int coupon_id;
    private int coupon_owner_id;
    private int restaurant_id;
    private int discount_percentage;
    private LocalDate expiration_date;
}
