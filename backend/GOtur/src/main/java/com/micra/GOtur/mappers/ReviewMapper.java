package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReviewMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {

        int review_id = rs.getInt("review_id");
        int purchase_id = rs.getInt("purchase_id");
        int reviewer_id = rs.getInt("reviewer_id");
        String comment = rs.getString("comment");
        float rate = rs.getFloat("rate");
        LocalDate review_date = rs.getDate("review_date").toLocalDate();

        Review review = new Review();
        review.setReview_id(review_id);
        review.setPurchase_id(purchase_id);
        review.setReviewer_id(reviewer_id);
        review.setComment(comment);
        review.setRate(rate);
        review.setReview_date(review_date);

        return review;
    }

}
