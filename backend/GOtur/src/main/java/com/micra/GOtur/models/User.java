package com.micra.GOtur.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private int user_id;
    private String username;
    private String hashed_password;
    private String password_salt;
    private String email;
    private String phone_number;
    private int age;
    private String gender;
}
