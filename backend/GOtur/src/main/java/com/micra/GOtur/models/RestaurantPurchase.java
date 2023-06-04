package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantPurchase {
    private Customer customer;
    private Address address;
    private Purchase purchase;
    private Restaurant restaurant;
    private List<PurchaseItem> purchaseItemList;
}
