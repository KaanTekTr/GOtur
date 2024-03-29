package com.micra.GOtur.helpers;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Getter
@Setter
public class HashPasswordHelper {
    private String password;
    private static HashPasswordHelper instance;
    //A private constructor so that helper can be called only by getInstance method below.
    private HashPasswordHelper( String password ) {
        this.password = password;
    }
    public static HashPasswordHelper getInstance() {
        if ( instance ==  null ) {
            instance = new HashPasswordHelper("");
        }
        return instance;
    }

    public String Hash() {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] hash = digester.digest(this.password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
