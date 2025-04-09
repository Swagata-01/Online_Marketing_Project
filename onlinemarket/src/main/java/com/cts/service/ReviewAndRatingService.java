package com.cts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.ReviewAndRatingDTO;
import com.cts.entity.Products;
import com.cts.entity.User;
import com.cts.exception.InvalidInputException;
import com.cts.entity.ReviewsAndRatings;
import com.cts.repository.ProductRepository;
import com.cts.repository.ReviewAndRatingRepository;
import com.cts.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewAndRatingService {

    @Autowired
    private ReviewAndRatingRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Create Review (Updated signature to avoid DTO in controller)
    public ReviewsAndRatings createReview(int productId, int userId, double rating, String review) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        ReviewsAndRatings newReview = new ReviewsAndRatings();
        newReview.setProducts(product);
        newReview.setUser(user);
        newReview.setRating(rating);
        newReview.setReview(review);
        newReview.setReviewActiveStatus(true);
        newReview.setReviewCreatedOn(Timestamp.from(Instant.now()));

        return reviewRepository.save(newReview);
    }

    // Update Review
    public ReviewsAndRatings updateReview(Long ratingId, Double rating, String review) {
        ReviewsAndRatings existingReview = reviewRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with ID: " + ratingId));

       
            existingReview.setRating(rating);
       
        if (review != null && !review.isEmpty()) {
            existingReview.setReview(review); 
        }
        existingReview.setReviewActiveStatus(true);
        existingReview.setReviewUpdateOn(Timestamp.from(Instant.now())); 
        return reviewRepository.save(existingReview);
    }

    // Get All Reviews
    public List<ReviewAndRatingDTO> getAllReviews() {
    	return reviewRepository.findAll().stream()
    	        .filter(review -> review.isReviewActiveStatus())
    	        .map(review -> {
            ReviewAndRatingDTO reviewDTO = new ReviewAndRatingDTO();
            reviewDTO.setRatingId(review.getRatingId());
            reviewDTO.setProducts(review.getProducts());
            reviewDTO.setUser(review.getUser());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setReview(review.getReview());
            reviewDTO.setReviewCreatedOn(review.getReviewCreatedOn());
            reviewDTO.setReviewUpdateOn(review.getReviewUpdateOn());
            reviewDTO.setReviewDeletedOn(review.getReviewDeletedOn());
            return reviewDTO;
        }).collect(Collectors.toList());
    }
    
    
    public List<ReviewAndRatingDTO> getReviewsByUserId(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidInputException("User not found"));
        List<ReviewsAndRatings> reviews = reviewRepository.findByUser(user);
        
        return reviews.stream()
                      .map(review -> new ReviewAndRatingDTO(review))
                      .collect(Collectors.toList());
    }

    
//    public List<ReviewAndRatingDTO> getUserReviews(int userId) throws InvalidInputException {
//        if (!userRepository.existsById(userId)) {
//            throw new InvalidInputException("User not found with ID: " + userId);
//        }
//        return reviewRepository.findByUserId(userId).stream()
//                .filter(review -> review.isReviewActiveStatus())
//                .map(review -> {
//                    ReviewAndRatingDTO reviewDTO = new ReviewAndRatingDTO();
//                    reviewDTO.setRatingId(review.getRatingId());
//                    reviewDTO.setProducts(review.getProducts());
//                    reviewDTO.setUser(review.getUser());
//                    reviewDTO.setRating(review.getRating());
//                    reviewDTO.setReview(review.getReview());
//                    reviewDTO.setReviewCreatedOn(review.getReviewCreatedOn());
//                    reviewDTO.setReviewUpdateOn(review.getReviewUpdateOn());
//                    return reviewDTO;
//                }).collect(Collectors.toList());
//    }
    
}
