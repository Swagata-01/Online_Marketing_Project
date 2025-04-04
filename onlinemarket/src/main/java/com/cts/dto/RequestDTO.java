package com.cts.dto;

import java.util.Date;

//import com.cts.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message= "Photo is required")
    private byte[] photo;
    
    @NotBlank(message = "Nickname is required")
    private String nickName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;
    
    @NotNull(message="Date of Birth is required")
    private Date dateOfBirth;

}

