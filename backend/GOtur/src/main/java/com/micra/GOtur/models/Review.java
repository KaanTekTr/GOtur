package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Review {

    private int review_id;
    private int purchase_id;
    private String comment;
    private float rate;
    private LocalDate review_date;

}
