package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.PurchaseGroupMapper;
import com.micra.GOtur.models.PurchaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchaseGroup")
@CrossOrigin
public class PurchaseGroupController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PurchaseGroupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<PurchaseGroup> getAllRestaurantOwners() {
        String sql = "SELECT * FROM PurchaseGroup P;";

        List<PurchaseGroup> list = jdbcTemplate.query(sql, new PurchaseGroupMapper());

        return list;
    }

    @GetMapping("/{groupId}")
    public PurchaseGroup getRestaurantOwner(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM PurchaseGroup P WHERE P.group_id = ?;";

        return jdbcTemplate.queryForObject(sql, new PurchaseGroupMapper(), groupId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPurchaseGroup(@RequestBody PurchaseGroup purchaseGroup) {
        String sql = "INSERT INTO PurchaseGroup(group_owner_id, group_name) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchaseGroup.getGroup_owner_id(), purchaseGroup.getGroup_name());

        return new ResponseEntity<>("Purchase Group Successfully Inserted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<String> deletePurchaseGroup(@PathVariable("groupId") int groupId) {

        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) {
            return new ResponseEntity<>("The Purchase Group with ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM PurchaseGroup P WHERE P.group_id = ?;";
        jdbcTemplate.update(sql, groupId);

        return new ResponseEntity<>("Purchase Group With ID: " + groupId + " has been deleted!",
                HttpStatus.OK);
    }
}
