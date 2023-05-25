package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Promote {

    private int promotion_id;
    private int promoter_id;
    private int restaurant_id;
    private String promotion_code;
    private int profit_rate;
    private LocalDate expiration_date;
    private int user_quota;

}
