package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.AdminMapper;
import com.micra.GOtur.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AdminController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        String sql = "SELECT * FROM Admin A, User U WHERE U.user_id = A.user_id;";

        List<Admin> list = jdbcTemplate.query(sql, new AdminMapper());

        return list;
    }

    @GetMapping("/{adminId}")
    public Admin getAdmin(@PathVariable("adminId") int adminId) {
        String sql = "SELECT * FROM Admin A, User U WHERE U.user_id = A.user_id AND U.user_id = ?;";

        return jdbcTemplate.queryForObject(sql, new AdminMapper(), adminId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        String sql = "INSERT INTO User(username, hashed_password, password_salt, email, phone_number, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, admin.getUsername(), admin.getHashed_password(), admin.getPassword_salt(),
                admin.getEmail(), admin.getPhone_number(), admin.getAge(), admin.getGender());

        int insertedId = getIdByEmail(admin.getEmail());

        String sql2 = "INSERT INTO Admin(user_id) VALUES (?);";
        System.out.println(">>" + sql2);
        jdbcTemplate.update(sql2, insertedId);

        return new ResponseEntity<>("Admin Successfully Inserted With ID: "+ insertedId + "!", HttpStatus.OK);
    }

    public int getIdByEmail(String email) {
        String sql = "SELECT user_id FROM USER U WHERE U.email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("adminId") int adminId) {
        String sql = "DELETE FROM USER U WHERE U.user_id = ?;";
        jdbcTemplate.update(sql, adminId);

        return new ResponseEntity<>("Admin With ID: " + adminId + " has been deleted!",
                HttpStatus.OK);
    }
}
