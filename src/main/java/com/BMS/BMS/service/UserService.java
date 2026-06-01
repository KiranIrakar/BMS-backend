package com.BMS.BMS.service;

import com.BMS.BMS.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    // ── Auth ────────────────────────────────────────────────────────────────
    AuthResponseDto signup(SignupRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);

    // ── OTP ─────────────────────────────────────────────────────────────────
    void sendOtp(OtpRequestDto dto);
    AuthResponseDto verifyOtp(OtpVerifyDto dto);

    // ── CRUD ────────────────────────────────────────────────────────────────
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto getUserById(String id);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto updateUser(String id, UserRequestDto dto);
    void deleteUser(String id);
}