package com.BMS.BMS.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponseDto {

    private String id;
    private String full_name;
    private String email;
    private String mobile_number;
    private Boolean verified;
    private String role;
    @JsonIgnore
    private LocalDateTime created_at;
    @JsonIgnore
    private LocalDateTime updated_at;
}
