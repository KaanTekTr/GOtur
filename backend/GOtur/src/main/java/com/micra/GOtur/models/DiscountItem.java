package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiscountItem {
    private DiscountCoupon discountCoupon;
    private Restaurant restaurant;
}
