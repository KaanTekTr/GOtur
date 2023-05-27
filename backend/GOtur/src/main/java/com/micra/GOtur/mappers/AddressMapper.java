package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        int address_id = rs.getInt("address_id");
        int customer_id = rs.getInt("customer_id");
        String address_name = rs.getString("address_name");
        Boolean is_primary = rs.getBoolean("is_primary");
        String city = rs.getString("city");
        String district = rs.getString("district");
        String street_num = rs.getString("street_num");
        String street_name = rs.getString("street_name");
        String building_num = rs.getString("building_num");
        String detailed_desc = rs.getString("detailed_desc");

        Address address = new Address();

        address.setAddress_id(address_id);
        address.setCustomer_id(customer_id);
        address.setAddress_name(address_name);
        address.setIs_primary(is_primary);
        address.setCity(city);
        address.setDistrict(district);
        address.setStreet_num(street_num);
        address.setStreet_name(street_name);
        address.setBuilding_num(building_num);
        address.setDetailed_desc(detailed_desc);

        return address;
    }
}
