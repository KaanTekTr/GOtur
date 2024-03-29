package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Report {

    private int report_id;
    private int admin_id;
    private String details;
    private String report_type;
    private LocalDateTime report_date;

}
