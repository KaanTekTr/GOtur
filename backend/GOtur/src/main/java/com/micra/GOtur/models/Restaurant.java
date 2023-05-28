package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    private int restaurant_id;
    private String restaurant_name;
    private String district;
    private float total_earnings;
    private String open_hour;
    private String close_hour;
    private int min_delivery_price;
    private Boolean is_top_restaurant;
    private float rating;

}
