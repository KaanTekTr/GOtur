package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private int user_id;
    private int age;
    private String gender;
    private String username;
    private String email;
    private String phone_number;

}
