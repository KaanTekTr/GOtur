package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiscountCoupon {

    private int coupon_id;
    private String coupon_owner_id;
    private int discount_percentage;

}
