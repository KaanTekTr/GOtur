package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseGroup {

    private int group_id;
    private int group_owner_id;
    private String group_name;
    private float group_balance;

}
