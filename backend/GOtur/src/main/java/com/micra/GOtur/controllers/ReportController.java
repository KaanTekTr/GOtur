package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.*;
import com.micra.GOtur.models.Admin;
import com.micra.GOtur.models.FoodCategory;
import com.micra.GOtur.models.Report;
import com.micra.GOtur.models.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@CrossOrigin
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReportController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @GetMapping("/all")
    public List<Report> getReports() {

        String sql = "SELECT * FROM Report R;";
        List<Report> allReports = jdbcTemplate.query(sql, new ReportMapper());
        return allReports;
    }

    @GetMapping("/{reportId}")
    public Report getReport(@PathVariable("reportId") int reportId) {
        String sql = "SELECT * FROM Report R WHERE R.report_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new ReportMapper(), reportId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/mostFavoritedRestaurant/{adminId}")
    public ResponseEntity<String> mostFavoritedRestaurant(
            @PathVariable("adminId") int adminId
    ) {

        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, adminId);
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + adminId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String restaurantSQL = "WITH temp(restaurant_id, totalCount) AS" +
                " (SELECT restaurant_id, count(*)" +
                " FROM Favorites" +
                " GROUP BY restaurant_id  )" +
                " SELECT *" +
                " FROM temp NATURAL JOIN Restaurant" +
                " WHERE totalCount = ( SELECT max(totalCount)" +
                " FROM temp);";
        List<Restaurant> restaurants = jdbcTemplate.query(restaurantSQL, new RestaurantMapper());

        StringBuilder detail = new StringBuilder();
        detail.append("Most favorited restaurants are: ");
        for (Restaurant r : restaurants) {
            detail.append(r.getRestaurant_name() + ", ");
        }
        int length = detail.length();
        detail.delete(length - 2, length);
        String detailStr = detail.toString();

        String sql = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sql, adminId, detailStr, "Most Favorited Restaurant", LocalDateTime.now());

        return new ResponseEntity<>(detailStr, HttpStatus.OK);
    }

    @PostMapping("/restaurantActiveCoupons/{adminId}/{restaurantId}")
    public ResponseEntity<String> restaurantActiveCoupons(
            @PathVariable("adminId") int adminId,
            @PathVariable("restaurantId") int restaurantId
    ) {

        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, adminId);
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + adminId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkRestaurant = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean existsRestaurant = jdbcTemplate.queryForObject(checkRestaurant, Boolean.class, restaurantId);
        if (!existsRestaurant) {
            return new ResponseEntity<>("Restaurant with id: " + restaurantId + " does not exist! Failed!", HttpStatus.BAD_REQUEST);
        }

        String p1 = "restaurant_name";
        String p2 = "count";
        String sql = "SELECT R.restaurant_name, count(*) AS count" +
                " FROM DiscountCoupon D NATURAL JOIN Restaurant R" +
                " WHERE (D.expiration_date >= ? AND R.restaurant_id = ?)" +
                " GROUP BY restaurant_id;";
        Map<String, Integer> map = jdbcTemplate.queryForObject(sql, new StringIntegerMapper(p1, p2), LocalDate.now(), restaurantId);

        StringBuilder detail = new StringBuilder();
        detail.append("");
        for (String key : map.keySet()) {
            detail.append("The restaurant with name '" + key + "' has total of "
                    + map.get(key) + " active coupons");
        }

        String sqlReport = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sqlReport, adminId, detail.toString(), "Restaurant Active Coupons", LocalDateTime.now());

        return new ResponseEntity<>(detail.toString(), HttpStatus.OK);

    }

    @PostMapping("/maxPurchaseInRegion/{adminId}")
    public ResponseEntity<String> restaurantActiveCoupons(
            @PathVariable("adminId") int adminId
    ) {

        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, adminId);
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + adminId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String p1 = "restaurant_name";
        String p2 = "district";
        String p3 = "purchaseCount";
        String sql = "WITH temp(restaurant_id, district, purchaseCount) AS" +
                " (" +
                " SELECT R.restaurant_id, R.district, count(purchase_id) AS purchaseCount" +
                " FROM Purchase O, Restaurant R" +
                " WHERE O.restaurant_id = R.restaurant_id" +
                " GROUP BY R.restaurant_id, R.district" +
                " )" +
                " SELECT R.restaurant_name, T.district, T.purchaseCount" +
                " FROM temp T NATURAL JOIN Restaurant R" +
                " WHERE T.purchaseCount = (SELECT max(purchaseCount)" +
                " FROM temp WHERE temp.district = R.district);";
        List<Map<List<String>, Integer>> ls = jdbcTemplate.query(sql, new StringStringIntegerMapper(p1, p2, p3));
        System.out.println("ls length: " + ls.size());
        StringBuilder detail = new StringBuilder();
        detail.append("");
        for (Map<List<String>, Integer> map : ls) {
            for (List<String> key : map.keySet()) {
                detail.append("The restaurant with name '" + key.get(0) + "' has most number of purchases in region '"
                        + key.get(1)  + "' " + "with a total of " + map.get(key) + " purchases\n");
            }
        }

        /*
        List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql);
        StringBuilder detail = new StringBuilder();
        detail.append("");
        String str1 = "";
        String str2 = "";
        String str3 = "";
        for (Map<String, Object> map : ls) {
            int ctr = 0;
            for (String key : map.keySet()) {
                if (ctr == 0) { str1 = map.get(key).toString(); }
                if (ctr == 1) { str2 = map.get(key).toString(); }
                if (ctr == 2) { str3 = map.get(key).toString(); }
                ctr++;
            }
        }
        System.out.println("ls length: " + ls.size());
        detail.append("The restaurant with name '" + str1 + "' has most number of purchases in region '"
                + str2  + "' " + "with a total of " + str3 + " purchases\n");
        */

        String sqlReport = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sqlReport, adminId, detail.toString(), "Restaurants With Most Number of Purchases in Region", LocalDateTime.now());

        return new ResponseEntity<>(detail.toString(), HttpStatus.OK);

    }

    @PostMapping("/maxRatingInRegion/{adminId}")
    public ResponseEntity<String> restaurantMaxRating(
            @PathVariable("adminId") int adminId
    ) {

        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, adminId);
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + adminId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String p1 = "restaurant_name";
        String p2 = "district";
        String p3 = "rating";
        String sql = "SELECT r1.restaurant_name, r1.district, r1.rating" +
                " FROM Restaurant r1" +
                " WHERE r1.rating = (" +
                " SELECT MAX(r2.rating)" +
                " FROM Restaurant r2" +
                " WHERE r1.district = r2.district" +
                " )" +
                " ORDER BY r1.district;";
        List<Map<List<String>, Integer>> ls = jdbcTemplate.query(sql, new StringStringIntegerMapper(p1, p2, p3));

        StringBuilder detail = new StringBuilder();
        detail.append("");
        for (Map<List<String>, Integer> map : ls) {
            for (List<String> key : map.keySet()) {
                detail.append("The restaurant with name '" + key.get(0) + "' has the highest rating in region '"
                        + key.get(1)  + "' " + "with a rating of " + map.get(key) + "\n");
            }
        }

        String sqlReport = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sqlReport, adminId, detail.toString(), "Restaurants With Highest Rating in Region", LocalDateTime.now());

        return new ResponseEntity<>(detail.toString(), HttpStatus.OK);
    }

    @PostMapping("/maxDiscountCoupon/{adminId}")
    public ResponseEntity<String> restaurantMaxDiscountCoupon(
            @PathVariable("adminId") int adminId
    ) {

        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, adminId);
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + adminId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String p1 = "restaurant_name";
        String p2 = "couponCount";
        String sql = "WITH temp( restaurant_id, couponCount ) AS (" +
                " SELECT restaurant_id, count(*) AS count" +
                " FROM DiscountCoupon" +
                " WHERE expiration_date >= ?" +
                " GROUP BY restaurant_id" +
                " )" +
                " SELECT R.restaurant_name, T.couponCount" +
                " FROM temp T NATURAL JOIN Restaurant R" +
                " WHERE couponCount = (SELECT MAX(couponCount)" +
                " FROM temp);";
        List<Map<String, Integer>> ls = jdbcTemplate.query(sql, new StringIntegerMapper(p1, p2), LocalDate.now());
        StringBuilder detail = new StringBuilder();
        detail.append("");
        for (Map<String, Integer> map : ls) {
            for (String key : map.keySet()) {
                detail.append("The restaurant with name '" + key + "' has the most number of active coupons with a total of "
                        + map.get(key) + " active coupons\n");
            }
        }

        String sqlReport = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sqlReport, adminId, detail.toString(), "Restaurant With Most Active Coupons", LocalDateTime.now());

        return new ResponseEntity<>(detail.toString(), HttpStatus.OK);

    }



    @DeleteMapping("/delete/{reportId}")
    public ResponseEntity<String> deleteReport(
            @PathVariable("reportId") int reportId
    ) {
        String sql = "DELETE FROM Report R WHERE R.report_id = (?);";
        jdbcTemplate.update(sql, reportId);

        return new ResponseEntity<>("Report with id: " + reportId +
                " is successfully deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllReports() {
        String sql = "DELETE FROM Report";
        jdbcTemplate.update(sql);
        return new ResponseEntity<>("All reports are successfully deleted!", HttpStatus.OK);
    }


}
