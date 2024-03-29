package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Review {

    private int review_id;
    private int purchase_id;
    private int reviewer_id;
    private String comment;
    private float rate;
    private LocalDateTime review_date;

}
