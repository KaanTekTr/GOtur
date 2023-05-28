package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.PromoteMapper;
import com.micra.GOtur.mappers.PromoterMapper;
import com.micra.GOtur.models.Promote;
import com.micra.GOtur.models.Promoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@CrossOrigin
public class PromotionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PromotionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*
     * promoter
     */

    @GetMapping("/allPromoters")
    public List<Promoter> getAllPromoters() {

        String sql = "SELECT * FROM Promoter P;";
        List<Promoter> allPromoters = jdbcTemplate.query(sql, new PromoterMapper());
        return allPromoters;
    }

    @GetMapping("/getPromoter/{promoterId}")
    public Promoter getPromoter(@PathVariable("promoterId") int promoterId) {
        String sql = "SELECT * FROM Promoter P WHERE P.promoter_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new PromoterMapper(), promoterId);
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping("/deletePromoter/{promoterId}")
    public ResponseEntity<String> deletePromoterById(
            @PathVariable("promoterId") int promoterId
    ) {
        String sql = "DELETE FROM Promoter P WHERE P.promoter_id = (?);";
        jdbcTemplate.update(sql, promoterId);

        return new ResponseEntity<>("Promoter with id: " + promoterId +
                " is successfully deleted!", HttpStatus.OK);
    }

    @PostMapping("/addPromoter")
    public ResponseEntity<String> addPromoter(
            @RequestBody Promoter promoter
    ) {
        String sqlPromoter = "INSERT INTO Promoter(promoter_id, income) VALUES (?,?);";
        jdbcTemplate.update(sqlPromoter, promoter.getPromoter_id(), promoter.getIncome());

        return new ResponseEntity<>("Promoter is successfully added!", HttpStatus.OK);
    }


    /*
     * promotion
     */

    @GetMapping("/allPromotions")
    public List<Promote> getAllPromotions() {

        String sql = "SELECT * FROM Promote P;";
        List<Promote> allPromotes = jdbcTemplate.query(sql, new PromoteMapper());
        return allPromotes;
    }

    @GetMapping("/getPromotion/{promotionId}")
    public Promote getPromotion(@PathVariable("promotionId") int promotionId) {
        String sql = "SELECT * FROM Promote P WHERE P.promotion_id = ? ;";

        try {
            return jdbcTemplate.queryForObject(sql, new PromoteMapper(), promotionId);
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping("/deletePromotion/{promotionId}")
    public ResponseEntity<String> deletePromotionById(
            @PathVariable("promotionId") int promotionId
    ) {
        String sql = "DELETE FROM Promote P WHERE P.promotion_id = (?);";
        jdbcTemplate.update(sql, promotionId);

        return new ResponseEntity<>("Promotion with id: " + promotionId +
                " is successfully deleted!", HttpStatus.OK);
    }


    @PostMapping("/addPromotion")
    public ResponseEntity<String> addPromotion(
            @RequestBody Promote promote
    ) {

        String restaurantDNE = "SELECT EXISTS (SELECT * FROM Restaurant R WHERE R.restaurant_id = ?);";
        boolean existsRestaurant = jdbcTemplate.queryForObject(restaurantDNE, Boolean.class, promote.getRestaurant_id());
        if (!existsRestaurant) {
            return new ResponseEntity<>("Restaurant with id: " + promote.getRestaurant_id() + " does not exist! Add failed!", HttpStatus.BAD_REQUEST);
        }

        String promoterDNE = "SELECT EXISTS (SELECT * FROM Promoter P WHERE P.promoter_id = ?);";
        boolean existsPromoter = jdbcTemplate.queryForObject(promoterDNE, Boolean.class, promote.getPromoter_id());
        if (!existsPromoter) {
            return new ResponseEntity<>("Promoter with id: " + promote.getPromoter_id() + " does not exist! Add failed!", HttpStatus.BAD_REQUEST);
        }

        String promoterForRestaurantExists = "SELECT EXISTS (SELECT * FROM Promote P WHERE (P.restaurant_id = ? AND P.promoter_id = ?));";
        boolean existsPromoterForRestaurant = jdbcTemplate.queryForObject(promoterForRestaurantExists, Boolean.class, promote.getRestaurant_id(), promote.getPromoter_id());
        if (existsPromoterForRestaurant) {
            return new ResponseEntity<>("Promoter with id: " + promote.getPromoter_id() + " already exists for Restaurant with id : " +
                    promote.getRestaurant_id() + " !", HttpStatus.BAD_REQUEST);
        }

        String sqlPromote = "INSERT INTO Promote(promoter_id, restaurant_id, promotion_code, profit_rate, expiration_date, user_quota) VALUES (?,?,?,?,?,?);";
        jdbcTemplate.update(sqlPromote, promote.getPromoter_id(), promote.getRestaurant_id(), promote.getPromotion_code(), promote.getProfit_rate(),
                promote.getExpiration_date(), promote.getUser_quota());

        return new ResponseEntity<>("Promotion is successfully added for Promoter with id: " + promote.getPromoter_id() + "! ", HttpStatus.OK);
    }


}
