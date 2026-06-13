package com.BMS.BMS.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginRequestDto {

    @NotBlank(message = "Email or mobile number is required")
    private String identifier;   // accepts email OR mobile_number

    @NotBlank(message = "Password is required")
    private String password;
}