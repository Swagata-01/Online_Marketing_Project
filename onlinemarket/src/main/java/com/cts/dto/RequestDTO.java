package com.cts.dto;

import java.time.LocalDate;
import java.util.Date;

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

    private byte[] photo; // Move to MultipartFile in controller layer

    @NotBlank(message = "Nickname is required")
    private String nickName;

    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;

    @NotBlank(message = "Address Line 2 is required")
    private String addressLine2;

    @NotBlank(message = "Postal Code is required")
    private int postalCode;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotNull(message = "Date of Birth is required")
    private Date dateOfBirth;

    public String getAddress() {
        return String.format("%s, %s, %s", addressLine1, addressLine2, postalCode);
    }
}
