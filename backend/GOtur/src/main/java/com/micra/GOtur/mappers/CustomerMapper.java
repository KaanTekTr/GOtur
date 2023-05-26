package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        int user_id = rs.getInt("user_id");
        String username = rs.getString("username");
        String hashed_password = rs.getString("hashed_password");
        String password_salt = rs.getString("password_salt");
        String email = rs.getString("email");
        String phone_number = rs.getString("phone_number");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        float balance = rs.getFloat("balance");
        float total_points = rs.getFloat("total_points");
        String payment_method = rs.getString("payment_method");

        Customer customer = new Customer();
        customer.setUser_id(user_id);
        customer.setUsername(username);
        customer.setHashed_password(hashed_password);
        customer.setPassword_salt(password_salt);
        customer.setEmail(email);
        customer.setPhone_number(phone_number);
        customer.setAge(age);
        customer.setGender(gender);
        customer.setBalance(balance);
        customer.setTotal_points(total_points);
        customer.setPayment_method(payment_method);

        return customer;
    }
}
