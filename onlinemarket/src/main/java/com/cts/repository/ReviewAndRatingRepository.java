package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.ReviewsAndRatings;
import com.cts.entity.User;

public interface ReviewAndRatingRepository extends JpaRepository<ReviewsAndRatings, Long> {

	List<ReviewsAndRatings> findByUser(User user);
	
}
