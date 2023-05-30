package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Customer;
import com.micra.GOtur.models.PromoterView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PromoterViewMapper implements RowMapper<PromoterView> {

    @Override
    public PromoterView mapRow(ResultSet rs, int rowNum) throws SQLException {
        String username = rs.getString("username");
        String email = rs.getString("email");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");

        PromoterView promoterView = new PromoterView();
        promoterView.setUsername(username);
        promoterView.setAge(age);
        promoterView.setGender(gender);
        promoterView.setEmail(email);

        return promoterView;
    }
}
