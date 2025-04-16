package com.cts.mapper;

import com.cts.dto.UserAdminDTO;
import com.cts.entity.User;

public class UserAdminMapper {

    public static User toEntity(UserAdminDTO userAdminDTO) {
        return User.builder()
            .firstName(userAdminDTO.getFirstName())
            .lastName(userAdminDTO.getLastName())
            .email(userAdminDTO.getEmail())
            .password(userAdminDTO.getPassword())
            .photo(userAdminDTO.getPhoto())
            .nickName(userAdminDTO.getNickName())
            .address(userAdminDTO.getAddress())
            .contactNumber(userAdminDTO.getContactNumber())
            .dateOfBirth(userAdminDTO.getDateOfBirth())
            .userRole(userAdminDTO.getUserRole())
            .build();
    }
}
