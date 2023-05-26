package com.micra.GOtur.mappers;

import com.micra.GOtur.models.PurchaseGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseGroupMapper implements RowMapper<PurchaseGroup> {

    @Override
    public PurchaseGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
        int group_id = rs.getInt("group_id");
        int group_owner_id = rs.getInt("group_owner_id");
        String group_name = rs.getString("group_name");
        float group_balance = rs.getFloat("group_balance");

        PurchaseGroup purchaseGroup = new PurchaseGroup();

        purchaseGroup.setGroup_id(group_id);
        purchaseGroup.setGroup_owner_id(group_owner_id);
        purchaseGroup.setGroup_name(group_name);
        purchaseGroup.setGroup_balance(group_balance);

        return purchaseGroup;
    }
}
