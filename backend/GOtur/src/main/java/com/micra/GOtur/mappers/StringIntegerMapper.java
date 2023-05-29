package com.micra.GOtur.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StringIntegerMapper implements RowMapper<Map<String, Integer>> {

    private String p1;
    private String p2;
    public StringIntegerMapper(String p1, String p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
        String myStr = rs.getString(p1);
        int myInt = rs.getInt(p2);

        Map<String, Integer> map = new HashMap<>();
        map.put(myStr, myInt);
        return map;
    }
}
