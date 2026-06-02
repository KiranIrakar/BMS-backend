package com.BMS.BMS.service.impl;

import com.BMS.BMS.dto.*;
import com.BMS.BMS.models.User;
import com.BMS.BMS.repository.UserRepository;
import com.BMS.BMS.security.JwtUtil;
import com.BMS.BMS.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ── Helpers ─────────────────────────────────────────────────────────────

    private UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .full_name(user.getFull_name())
                .email(user.getEmail())
                .mobile_number(user.getMobileNumber())
                .verified(user.getVerified())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .updated_at(user.getUpdated_at())
                .build();
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // ── Auth ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public AuthResponseDto signup(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByMobileNumber(dto.getMobile_number())) {
            throw new RuntimeException("Mobile number already registered");
        }

        User user = User.builder()
                .full_name(dto.getFull_name())
                .email(dto.getEmail())
                .mobileNumber(dto.getMobile_number())
                .password(passwordEncoder.encode(dto.getPassword()))
                .verified(false)
                .role("ROLE_USER")
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponseDto(token, toResponseDto(user));
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository
                .findByEmailOrMobileNumber(dto.getIdentifier(), dto.getIdentifier())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponseDto(token, toResponseDto(user));
    }

    // ── OTP ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void sendOtp(OtpRequestDto dto) {
        User user = userRepository
                .findByMobileNumber(dto.getMobile_number())
                .orElseThrow(() -> new RuntimeException("User not found with this mobile number"));

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtp_expiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        // TODO: Integrate SMS gateway (Twilio / MSG91) to send OTP
        // For now, log it (remove in production)
        log.info("OTP for {} : {}", dto.getMobile_number(), otp);
    }

    @Override
    @Transactional
    public AuthResponseDto verifyOtp(OtpVerifyDto dto) {
        User user = userRepository
                .findByMobileNumber(dto.getMobile_number())
                .orElseThrow(() -> new RuntimeException("User not found with this mobile number"));

        if (user.getOtp() == null || !user.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        if (user.getOtp_expiry() == null || LocalDateTime.now().isAfter(user.getOtp_expiry())) {
            throw new RuntimeException("OTP has expired");
        }

        // Mark verified and clear OTP fields
        user.setVerified(true);
        user.setOtp(null);
        user.setOtp_expiry(null);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponseDto(token, toResponseDto(user));
    }

    // ── CRUD ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByMobileNumber(dto.getMobile_number())) {
            throw new RuntimeException("Mobile number already registered");
        }

        User user = User.builder()
                .full_name(dto.getFull_name())
                .email(dto.getEmail())
                .mobileNumber(dto.getMobile_number())
                .password(passwordEncoder.encode(dto.getPassword()))
                .verified(false)
                .role(dto.getRole() != null ? dto.getRole() : "ROLE_USER")
                .build();

        return toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponseDto);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(String id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check duplicates only if changed
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        if (!user.getMobileNumber().equals(dto.getMobile_number())
                && userRepository.existsByMobileNumber(dto.getMobile_number())) {
            throw new RuntimeException("Mobile number already in use");
        }

        user.setFull_name(dto.getFull_name());
        user.setEmail(dto.getEmail());
        user.setMobileNumber(dto.getMobile_number());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            user.setRole(dto.getRole());
        }

        return toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}