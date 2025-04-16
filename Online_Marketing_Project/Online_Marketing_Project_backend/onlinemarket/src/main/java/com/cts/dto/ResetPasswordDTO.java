package com.cts.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordDTO
{
    private String email;
    private String newPassword;
    private String confirmPassword;
}
 