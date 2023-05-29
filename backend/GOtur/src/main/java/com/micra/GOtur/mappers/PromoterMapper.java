package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Promoter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PromoterMapper implements RowMapper<Promoter> {

    @Override
    public Promoter mapRow(ResultSet rs, int rowNum) throws SQLException {
        int user_id = rs.getInt("user_id");
        String username = rs.getString("username");
        String hashed_password = rs.getString("hashed_password");
        String password_salt = rs.getString("password_salt");
        String email = rs.getString("email");
        String phone_number = rs.getString("phone_number");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        int income = rs.getInt("income");

        Promoter promoter = new Promoter();
        promoter.setUser_id(user_id);
        promoter.setUsername(username);
        promoter.setHashed_password(hashed_password);
        promoter.setPassword_salt(password_salt);
        promoter.setEmail(email);
        promoter.setPhone_number(phone_number);
        promoter.setAge(age);
        promoter.setGender(gender);
        promoter.setIncome(income);

        return promoter;
    }
}
