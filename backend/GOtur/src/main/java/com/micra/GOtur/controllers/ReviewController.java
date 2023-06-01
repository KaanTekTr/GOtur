package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.ReviewMapper;
import com.micra.GOtur.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReviewController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<Review> getAllReviews() {

        String sql = "SELECT * FROM Review R;";
        List<Review> allReviews = jdbcTemplate.query(sql, new ReviewMapper());
        return allReviews;
    }

    @GetMapping("/{reviewId}")
    public Review getReview(
            @PathVariable("reviewId") int reviewId) {

        String sql = "SELECT * FROM Review R WHERE R.review_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, new ReviewMapper(), reviewId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/purchase/{purchaseId}")
    public Review getReviewByPurchaseId(
            @PathVariable("purchaseId") int purchaseId
    ) {
        String sql = "SELECT * FROM Review R WHERE R.purchase_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, new ReviewMapper(), purchaseId);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(
            @RequestBody Review review) {

        String checkPurchase = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ?);";
        boolean existsPurchase = jdbcTemplate.queryForObject(checkPurchase, Boolean.class, review.getPurchase_id());
        if (!existsPurchase) {
            return new ResponseEntity<>("Purchase with id: " + review.getPurchase_id() + " does not exist! Add review failed!", HttpStatus.BAD_REQUEST);
        }

        String checkReviewer = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean existsReviewer = jdbcTemplate.queryForObject(checkReviewer, Boolean.class, review.getReviewer_id());
        if (!existsReviewer) {
            return new ResponseEntity<>("Customer (Reviewer) with id: " + review.getReviewer_id() + " does not exist! Add review failed!", HttpStatus.BAD_REQUEST);
        }

        String checkGroup = "SELECT EXISTS (SELECT * FROM Review R WHERE R.reviewer_id = ? AND R.purchase_id = ?);";
        boolean existsGroup = jdbcTemplate.queryForObject(checkGroup, Boolean.class, review.getReviewer_id(), review.getPurchase_id());
        if (existsGroup) {
            return new ResponseEntity<>("A Review by that Customer is already made", HttpStatus.BAD_REQUEST);
        }

        String checkIsDelivered = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_delivered = false);";
        boolean notDelivered = jdbcTemplate.queryForObject(checkIsDelivered, Boolean.class, review.getPurchase_id());
        if (notDelivered) {
            return new ResponseEntity<>("Purchase is not delivered yet. Cannot make a review yet", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Review(purchase_id, reviewer_id, comment, rate, review_date) VALUES (?,?,?,?,?);";
        jdbcTemplate.update(sql, review.getPurchase_id(), review.getReviewer_id(), review.getComment(), review.getRate(), LocalDateTime.now());

        return new ResponseEntity<>("Review is successfully inserted!", HttpStatus.OK);

    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable("reviewId") int reviewId
    ) {
        String sql = "DELETE FROM Review R WHERE R.review_id = (?);";
        jdbcTemplate.update(sql, reviewId);

        return new ResponseEntity<>("Review with id: " + reviewId + " has been deleted!",
                HttpStatus.OK);
    }
}
