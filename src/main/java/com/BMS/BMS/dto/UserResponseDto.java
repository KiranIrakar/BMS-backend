package com.BMS.BMS.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String id;
    private String full_name;
    private String email;
    private String mobile_number;
    private Boolean verified;
    private String role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}