package com.BMS.BMS.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String token;
    private String tokenType = "Bearer";
    private UserResponseDto user;

    public AuthResponseDto(String token, UserResponseDto user) {
        this.token = token;
        this.user = user;
    }
}