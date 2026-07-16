package com.example.whitefox.auth.repository;

import com.example.whitefox.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByPhoneAndRoleAndOtpAndVerifiedFalse(String phone, String role, String otp);
    Optional<OtpVerification> findTopByPhoneAndRoleOrderByExpiresAtDesc(String phone, String role);
}
