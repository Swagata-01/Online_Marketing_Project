package com.cts.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class SignInResponse {
    private String message;
    private boolean success;
}
