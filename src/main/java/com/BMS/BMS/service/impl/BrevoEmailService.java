package com.BMS.BMS.service.impl;

import com.BMS.BMS.config.BrevoProperties;
import com.BMS.BMS.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrevoEmailService implements EmailService {

    private static final String BREVO_SEND_EMAIL_URL = "https://api.brevo.com/v3/smtp/email";

    private final BrevoProperties brevoProperties;

    @Override
    public void sendOtpEmail(String toEmail, String otp, String fullName) {
        if (isBlank(brevoProperties.getApiKey()) || isBlank(brevoProperties.getFromEmail())) {
            log.warn("Brevo email is not configured. OTP email to {} was skipped.", toEmail);
            return;
        }

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", brevoProperties.getFromName(),
                        "email", brevoProperties.getFromEmail()),
                "to", List.of(Map.of(
                        "email", toEmail,
                        "name", isBlank(fullName) ? toEmail : fullName)),
                "subject", "Your BMS verification OTP",
                "htmlContent", buildOtpEmailHtml(otp, fullName));

        RestClient.create()
                .post()
                .uri(BREVO_SEND_EMAIL_URL)
                .header("api-key", brevoProperties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private String buildOtpEmailHtml(String otp, String fullName) {
        String greeting = isBlank(fullName) ? "Hello" : "Hello " + fullName;
        return """
                <p>%s,</p>
                <p>Your BMS verification OTP is:</p>
                <h2>%s</h2>
                <p>This OTP will expire in 10 minutes.</p>
                """.formatted(greeting, otp);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
