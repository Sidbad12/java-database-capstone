package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Generate Token (valid for 7 days)
    public String generateToken(String identifier, String role) {
        return Jwts.builder()
                .setSubject(identifier) // email or username
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract identifier (email / username)
    public String extractIdentifier(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate token belongs to correct user type
    public boolean validateToken(String token, String userType) {
        try {
            String identifier = extractIdentifier(token);

            if (userType.equalsIgnoreCase("admin")) {
                Admin admin = adminRepository.findByUsername(identifier);
                return admin != null;
            }

            if (userType.equalsIgnoreCase("doctor")) {
                Doctor doctor = doctorRepository.findByEmail(identifier);
                return doctor != null;
            }

            if (userType.equalsIgnoreCase("patient")) {
                Patient patient = patientRepository.findByEmail(identifier);
                return patient != null;
            }

            return false;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Get JWT signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
