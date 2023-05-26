package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Admin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper implements RowMapper<Admin> {

    @Override
    public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
        int user_id = rs.getInt("user_id");
        String username = rs.getString("username");
        String hashed_password = rs.getString("hashed_password");
        String password_salt = rs.getString("password_salt");
        String email = rs.getString("email");
        String phone_number = rs.getString("phone_number");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        int report_count = rs.getInt("report_count");

        Admin admin = new Admin();
        admin.setUser_id(user_id);
        admin.setUsername(username);
        admin.setHashed_password(hashed_password);
        admin.setPassword_salt(password_salt);
        admin.setEmail(email);
        admin.setPhone_number(phone_number);
        admin.setAge(age);
        admin.setGender(gender);
        admin.setReport_count(report_count);

        return admin;
    }
}
