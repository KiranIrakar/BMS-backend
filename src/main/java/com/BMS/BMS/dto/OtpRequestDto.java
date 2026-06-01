package com.BMS.BMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequestDto {

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    @NotBlank(message = "Mobile number is required")
    private String mobile_number;
}