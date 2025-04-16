package com.cts.dto;

import java.sql.Timestamp;

import com.cts.entity.Products;
import com.cts.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAndRatingDTO {
	private long ratingId;
    private Products products;
    private User user;
    private double rating;
    private String review;
    private Timestamp reviewCreatedOn;
    private Timestamp reviewUpdateOn;
    private Timestamp reviewDeletedOn;
    private boolean reviewActiveStatus;

}
