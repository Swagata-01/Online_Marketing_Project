package com.cts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.ReviewAndRatingDTO;
import com.cts.entity.ReviewsAndRatings;
import com.cts.exception.InvalidInputException;
import com.cts.service.ReviewAndRatingService;

import java.util.List;

@RestController
@RequestMapping("/OMP/reviews")
public class ReviewAndRatingController {

    @Autowired
    private ReviewAndRatingService reviewService;

    // Create Review (All parameters as RequestParam)//reviewActiveStatus modify as user should not assign that
    @PostMapping("/createReview")
    public ResponseEntity<ReviewsAndRatings> createReview(
        @RequestParam int productId,
        @RequestParam int userId,
        @RequestParam double rating,
        @RequestParam String review
    ) {
        try {
            ReviewsAndRatings savedReview = reviewService.createReview(productId, userId, rating, review);
            return ResponseEntity.status(201).body(savedReview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update Review // Modify if user don't want to update rating but review only
    @PutMapping("/updateReview/{ratingId}")
    public ResponseEntity<ReviewsAndRatings> updateReview(
        @PathVariable Long ratingId,
        @RequestParam(required = false) double rating,
        @RequestParam(required = false) String review
        
    ) {
        try {
            ReviewsAndRatings updatedReview = reviewService.updateReview(ratingId, rating, review);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getReviews")
    public ResponseEntity<List<ReviewAndRatingDTO>> getAllReviews() {
        List<ReviewAndRatingDTO> reviews = reviewService.getAllReviews();
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }
    
    //Add reviews based on user ID
    
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<ReviewAndRatingDTO>> getUserReviews(@PathVariable int userId) {
//        try {
//            List<ReviewAndRatingDTO> userReviews = reviewService.getUserReviews(userId);
//            return ResponseEntity.ok(userReviews);
//        } catch (InvalidInputException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewAndRatingDTO>> getUserReviews(@PathVariable int userId) {
        try {
            List<ReviewAndRatingDTO> userReviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(userReviews);
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}
