package com.cts.dto;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.Date;
 
import com.cts.enums.UserRole;
 
import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@AllArgsConstructor

@NoArgsConstructor

@Builder

public class ResponseDTO {

    private Integer userID;

    private String firstName;

    private String lastName;

    private String email;

    private String nickName;

    private String address;

    private String contactNumber;

    private Date dateOfBirth; // Use LocalDate instead of Date

    private String photo; // Typically, you'd use a URL instead of binary data

    private UserRole userRole;

    private boolean emailVerification;

    private boolean isActive;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

}

 