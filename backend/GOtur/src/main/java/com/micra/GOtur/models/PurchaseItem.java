package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseItem {
    private Food food;
    private List<Ingredient> ingredientList;
    private int food_order;
}
