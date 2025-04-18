package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.dto.ReviewAndRatingDTO;
import com.cts.entity.ReviewsAndRatings;
import com.cts.entity.User;

public interface ReviewAndRatingRepository extends JpaRepository<ReviewsAndRatings, Long> {
//	List<ReviewsAndRatings> findByUserId(int userId);
//	List<ReviewsAndRatings> findByProductId(int productId);
	
	@Query(value = "SELECT product_id, MAX(rating) as max_rating FROM reviews_and_ratings " +
            "WHERE review_active_status = 1 " +
            "GROUP BY product_id " +
            "ORDER BY max_rating DESC " +
            "LIMIT 2", nativeQuery = true)
	List<Object[]> findTopRatedProducts();
	List<ReviewsAndRatings> findByUser(User user);
}
