package com.BMS.BMS.controller;

import com.BMS.BMS.dto.*;
import com.BMS.BMS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final UserService userService;

    // POST /api/otp/send
    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto<Void>> sendOtp(
            @Valid @RequestBody OtpRequestDto dto) {

        userService.sendOtp(dto);
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent successfully", null));
    }

    // POST /api/otp/verify
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> verifyOtp(
            @Valid @RequestBody OtpVerifyDto dto) {

        AuthResponseDto response = userService.verifyOtp(dto);
        return ResponseEntity.ok(ApiResponseDto.success("OTP verified successfully", response));
    }
}