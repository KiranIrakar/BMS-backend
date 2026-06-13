package com.BMS.BMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRequestDto {

    @NotBlank(message = "Full name is required")
    private String full_name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    @NotBlank(message = "Mobile number is required")
    private String mobile_number;

    @NotBlank(message = "Password is required")
    private String password;

    private String role;

    public UserRequestDto() {}

    public UserRequestDto(String full_name, String email, String mobile_number, String password, String role) {
        this.full_name = full_name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.password = password;
        this.role = role;
    }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile_number() { return mobile_number; }
    public void setMobile_number(String mobile_number) { this.mobile_number = mobile_number; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}