package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.AdminMapper;
import com.micra.GOtur.mappers.FoodCategoryMapper;
import com.micra.GOtur.mappers.ReportMapper;
import com.micra.GOtur.models.Admin;
import com.micra.GOtur.models.FoodCategory;
import com.micra.GOtur.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Report getFoodCategory(@PathVariable("reportId") int reportId) {
        String sql = "SELECT * FROM Report R WHERE R.report_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new ReportMapper(), reportId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReport(
            @RequestBody Report report
    ) {
        String checkAdmin = "SELECT EXISTS (SELECT * FROM Admin A WHERE A.user_id = ?);";
        boolean existsAdmin = jdbcTemplate.queryForObject(checkAdmin, Boolean.class, report.getAdmin_id());
        if (!existsAdmin) {
            return new ResponseEntity<>("Admin with id: " + report.getAdmin_id() + " does not exist! Add review failed!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Report(admin_id, details, report_type, report_date) VALUES (?,?,?,?);";
        jdbcTemplate.update(sql, report.getAdmin_id(), report.getDetails(), report.getReport_type(), report.getReport_date());

        // increment admin's report count
        String sqlUpdate = "UPDATE Admin SET report_count = ? WHERE user_id = ?;";
        String findAdmin = "SELECT * FROM Admin A, User U WHERE (U.user_id = ? AND U.user_id = A.user_id);";
        Admin admin = jdbcTemplate.queryForObject(findAdmin, new AdminMapper(), report.getAdmin_id());
        jdbcTemplate.update(sqlUpdate, admin.getReport_count()+1, report.getAdmin_id());

        return new ResponseEntity<>("Report is successfully inserted!", HttpStatus.OK);
    }

    @PostMapping("/analyze/{reportId}/{restaurantId}")
    public ResponseEntity<String> analyzeRestaurant(
            @PathVariable("reportId") int reportId,
            @PathVariable("restaurantId") int restaurantId
    ) {

        String checkReport = "SELECT EXISTS (SELECT * FROM Report R WHERE R.report_id = ?);";
        boolean existsReport = jdbcTemplate.queryForObject(checkReport, Boolean.class, reportId);
        if (!existsReport) {
            return new ResponseEntity<>("Report with id: " + reportId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkRestaurant = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean existsRestaurant = jdbcTemplate.queryForObject(checkRestaurant, Boolean.class, restaurantId);
        if (!existsRestaurant) {
            return new ResponseEntity<>("Restaurant with id: " + restaurantId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Analyzes(report_id, restaurant_id) VALUES (?,?);";
        jdbcTemplate.update(sql, reportId, restaurantId);

        return new ResponseEntity<>("Analysis is successfully inserted!", HttpStatus.OK);
    }

    @GetMapping("/all/{restaurantId}")
    public List<Report> getReportsOfRestaurant(@PathVariable("restaurantId") int restaurantId) {
        String sql = "SELECT * FROM Report R WHERE R.report_id IN (SELECT A.report_id FROM Analyzes A WHERE A.restaurant_id = ?);";
        List<Report> reports = jdbcTemplate.query(sql, new ReportMapper(), restaurantId);
        return reports;
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


}
