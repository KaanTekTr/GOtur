package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodInPurchase {

    private int food_id;
    private int purchase_id;
    private int food_order;
}
