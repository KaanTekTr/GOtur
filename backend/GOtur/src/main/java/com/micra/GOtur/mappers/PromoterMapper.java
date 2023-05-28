package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Promoter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PromoterMapper implements RowMapper<Promoter> {

    @Override
    public Promoter mapRow(ResultSet rs, int rowNum) throws SQLException {
        int promoter_id = rs.getInt("promoter_id");
        int income = rs.getInt("income");

        Promoter promoter = new Promoter();
        promoter.setPromoter_id(promoter_id);
        promoter.setIncome(income);

        return promoter;
    }
}
