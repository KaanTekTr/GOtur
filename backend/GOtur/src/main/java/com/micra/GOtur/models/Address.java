package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {

    private int address_id;
    private int customer_id;
    private boolean is_primary;
    private String city;
    private String district;
    private String street_num;
    private String street_name;
    private String building_num;
    private String detailed_desc;
}
