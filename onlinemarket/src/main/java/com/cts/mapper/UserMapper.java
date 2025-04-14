package com.cts.mapper;

import com.cts.entity.User;

import org.springframework.stereotype.Component;

import com.cts.dto.RequestDTO;
import com.cts.dto.ResponseDTO;

@Component
public class UserMapper {
    public static User toEntity(RequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
 
        return User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .nickName(requestDTO.getNickName())
                .address(requestDTO.getAddress())
                .contactNumber(requestDTO.getContactNumber())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .build();

    }
 
    public static ResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
 
        return ResponseDTO.builder()
                .userID(user.getUserID())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .address(user.getAddress())
                .photo("http://localhost:8090/OMP/user/image/" + user.getUserID())
                .contactNumber(user.getContactNumber())
                .dateOfBirth(user.getDateOfBirth())
                .userRole(user.getUserRole())
                .emailVerification(user.isEmailVerification())
                .isActive(user.isActive())
                .createdOn(user.getAddedOn())
                .updatedOn(user.getUpdatedOn())
                .build();

    }

}
 