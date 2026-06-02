package com.BMS.BMS.controller;

import com.BMS.BMS.dto.*;
import com.BMS.BMS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ── Signup & Login ───────────────────────────────────────────────────────

    // POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto dto) {

        AuthResponseDto response = userService.signup(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("User registered successfully", response));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto) {

        AuthResponseDto response = userService.login(dto);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", response));
    }

    // ── OTP ──────────────────────────────────────────────────────────────────

    // POST /api/auth/otp/send
    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponseDto<Void>> sendOtp(
            @Valid @RequestBody OtpRequestDto dto) {

        userService.sendOtp(dto);
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent successfully", null));
    }

    // POST /api/auth/otp/verify
    @PostMapping("/otp/verify")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> verifyOtp(
            @Valid @RequestBody OtpVerifyDto dto) {

        AuthResponseDto response = userService.verifyOtp(dto);
        return ResponseEntity.ok(ApiResponseDto.success("OTP verified successfully", response));
    }
}