package com.cts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reviews_and_Ratings", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "productId"}))
public class ReviewsAndRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ratingId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="userId", referencedColumnName = "userid")
    @JsonBackReference
    private User user;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="productId")
    @JsonBackReference
    private Products products;
    
    @Column(name = "reviewActive_status", nullable = false)
    private boolean reviewActiveStatus = true;
    
    private double rating;
    private String review;
    private Timestamp reviewCreatedOn;
    private Timestamp reviewUpdateOn;
    private Timestamp reviewDeletedOn;

}
