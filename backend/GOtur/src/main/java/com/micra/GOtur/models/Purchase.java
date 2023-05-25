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
    private boolean is_paid;
    private boolean is_group_purchase;
    private boolean being_prepared;
    private boolean is_departed;
    private boolean is_delivered;
    private boolean is_canceled;
    private LocalDate purchase_time;
    private float total_price;

}
