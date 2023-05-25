package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Food {

    private int food_id;
    private int food_category_id;
    private int restaurant_id;
    private int menu_category_id;
    private String name;
    private String fixed_ingredients;
    private int price;

}
