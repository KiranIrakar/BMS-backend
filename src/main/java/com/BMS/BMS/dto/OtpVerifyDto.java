package com.BMS.BMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class OtpVerifyDto {

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    @NotBlank(message = "Mobile number is required")
    private String mobile_number;

    @NotBlank(message = "OTP is required")
    private String otp;
}