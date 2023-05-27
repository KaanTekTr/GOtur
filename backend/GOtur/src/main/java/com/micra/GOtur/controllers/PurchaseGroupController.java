package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.CustomerMapper;
import com.micra.GOtur.mappers.PurchaseGroupMapper;
import com.micra.GOtur.models.Address;
import com.micra.GOtur.models.Customer;
import com.micra.GOtur.models.PurchaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<PurchaseGroup> getAllPurchaseGroups() {
        String sql = "SELECT * FROM PurchaseGroup P;";

        List<PurchaseGroup> list = jdbcTemplate.query(sql, new PurchaseGroupMapper());

        return list;
    }

    @GetMapping("/{groupId}")
    public PurchaseGroup getPurchaseGroup(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM PurchaseGroup P WHERE P.group_id = ?;";

        return jdbcTemplate.queryForObject(sql, new PurchaseGroupMapper(), groupId);
    }

    @GetMapping("/allMembers/{groupId}")
    public List<Customer> getAllGroupMembers(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM Customer C, User U WHERE C.user_id = U.user_id AND C.user_id IN (SELECT F.group_member_id FROM Forms F WHERE F.group_id = ?)";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerMapper(), groupId);

        return list;
    }

    @GetMapping("/groupOwner/{groupId}")
    public Customer getGroupOwnerByGroupId(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM Customer C, User U WHERE C.user_id = U.user_id AND C.user_id IN (SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?)";
        return jdbcTemplate.queryForObject(sql, new CustomerMapper(), groupId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPurchaseGroup(@RequestBody PurchaseGroup purchaseGroup) {

        SimpleJdbcInsert insertIntoGroup = new SimpleJdbcInsert(jdbcTemplate).withTableName("PurchaseGroup").usingGeneratedKeyColumns("group_id");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("group_owner_id", purchaseGroup.getGroup_owner_id());
        parameters.put("group_name", purchaseGroup.getGroup_name());
        parameters.put("group_balance", 0f);

        int groupId = insertIntoGroup.executeAndReturnKey(parameters).intValue();

        String sql1 = "INSERT INTO Forms(group_id, group_member_id) VALUES (?, ?);";

        System.out.println(">>" + sql1);
        jdbcTemplate.update(sql1, groupId, purchaseGroup.getGroup_owner_id());

        return new ResponseEntity<>("Purchase Group Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/add/{groupId}/{customerId}")
    public ResponseEntity<String> addCustomerToGroup(@PathVariable("groupId") int groupId, @PathVariable("customerId") int customerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, customerId);

        if (!exists1) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM PurchaseGroup P, Forms F WHERE P.group_id = F.group_id AND F.group_member_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, customerId);

        if (exists2) { // if the customer is already in the group
            return new ResponseEntity<>("Customer With ID: " + customerId + " already exists in the Purchase Group!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Forms(group_id, group_member_id) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, groupId, customerId);

        return new ResponseEntity<>("Customer is Successfully Inserted Into The Purchase Group!", HttpStatus.OK);
    }

    @PostMapping("/addAddress/{groupId}")
    public ResponseEntity<String> addAddressByPurchaseGroupId(@PathVariable("groupId") int groupId,
                                                              @RequestBody Address address) {
        // Check if the group exists
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // Get the ID of the group owner
        String groupOwnerSql = "SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?;";
        int groupOwnerId = jdbcTemplate.queryForObject(groupOwnerSql, Integer.class, groupId);

        String sql = "INSERT INTO Address(customer_id, address_name, is_primary, city, district, street_num, street_name, building_num, detailed_desc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        System.out.println(address.getIs_primary());
        jdbcTemplate.update(sql, groupOwnerId, address.getAddress_name(), address.getIs_primary(), address.getCity(), address.getDistrict(), address.getStreet_num(),
                address.getStreet_name(), address.getBuilding_num(), address.getDetailed_desc());

        return new ResponseEntity<>("Address Is Successfully Added To The Purchase Group!", HttpStatus.OK);
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

    @DeleteMapping("/delete/{groupId}/{customerId}")
    public ResponseEntity<String> deleteCustomerFromGrouo(@PathVariable("groupId") int groupId, @PathVariable("customerId") int customerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, customerId);

        if (!exists1) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM PurchaseGroup P, Forms F WHERE P.group_id = F.group_id AND F.group_member_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, customerId);

        if (!exists2) { // if the customer is already in the group
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist in the Purchase Group!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Forms F WHERE F.group_id = ? AND F.group_member_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, groupId, customerId);

        return new ResponseEntity<>("Customer is Successfully Deleted From The Purchase Group!", HttpStatus.OK);
    }
}
