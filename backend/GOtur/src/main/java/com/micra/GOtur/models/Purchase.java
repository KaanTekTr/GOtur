package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Purchase {

    private int purchase_id;
    private int customer_id;
    private int address_id;
    private int restaurant_id;
    private String customer_note;
    private Boolean is_paid;
    private Boolean is_group_purchase;
    private Boolean being_prepared;
    private Boolean is_departed;
    private Boolean is_delivered;
    private Boolean is_canceled;
    private LocalDate purchase_time;
    private float total_price;

}
