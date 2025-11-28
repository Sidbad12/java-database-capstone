package com.project.back_end.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenValidationService {

    // Mock method — replace with real JWT or DB validation later
    public Map<String, String> validateToken(String token, String role) {
        Map<String, String> errors = new HashMap<>();

        if (token == null || token.isEmpty()) {
            errors.put("error", "Token is missing");
            return errors;
        }

        // Example logic: tokens starting with "admin_" or "doctor_"
        if (role.equals("admin") && !token.startsWith("admin_")) {
            errors.put("error", "Invalid admin token");
        } else if (role.equals("doctor") && !token.startsWith("doctor_")) {
            errors.put("error", "Invalid doctor token");
        }

        return errors; // Empty = valid token
    }
}
