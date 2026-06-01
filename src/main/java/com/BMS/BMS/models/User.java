package com.BMS.BMS.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String full_name;

    @Column(unique = true)
    private String email;

    @Column(name = "mobile_number", unique = true)
    private String mobile_number;

    private String password;

    @Column(name = "is_verified")
    private Boolean verified = false;

    private String role = "ROLE_USER";

    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otp_expiry;

    private String temporary_phone;

    @CreationTimestamp
    @Column( updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}