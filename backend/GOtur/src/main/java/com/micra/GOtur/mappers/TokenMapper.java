package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Token;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TokenMapper implements RowMapper<Token> {
    @Override
    public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
        int token_id = rs.getInt("token_id");
        int user_id = rs.getInt("user_id");
        String token = rs.getString("token");
        boolean is_actively_used = rs.getBoolean("is_actively_used");
        LocalDateTime last_active = rs.getObject("last_active", LocalDateTime.class);

        Token Token = new Token();
        Token.set_actively_used(is_actively_used);
        Token.setToken(token);
        Token.setToken_id(token_id);
        Token.setUser_id(user_id);
        Token.setLast_active(last_active);
        return Token;
    }
}
