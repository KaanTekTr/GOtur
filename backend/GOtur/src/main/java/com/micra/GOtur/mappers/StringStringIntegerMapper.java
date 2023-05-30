package com.micra.GOtur.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringStringIntegerMapper implements RowMapper<Map<List<String>, Integer>> {

    private String p1;
    private String p2;
    private String p3;
    public StringStringIntegerMapper(String p1, String p2, String p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @Override
    public Map<List<String>, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
        String myStr = rs.getString(p1);
        String myStr2 = rs.getString(p2);
        int myInt = rs.getInt(p3);

        Map<List<String>, Integer> map = new HashMap<>();
        List<String> ls = new ArrayList<>();
        ls.add(myStr);
        ls.add(myStr2);
        map.put(ls, myInt);
        return map;
    }
}
