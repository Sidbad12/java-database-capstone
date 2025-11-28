package com.project.back_end.mvc;

import com.project.back_end.services.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private TokenValidationService tokenValidationService;

    // --- Admin Dashboard ---
    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        Map<String, Object> validation = tokenValidationService.validateToken(token, "admin");

        if (validation.isEmpty()) {
            // Token is valid → Load admin dashboard
            return new ModelAndView("admin/adminDashboard");
        } else {
            // Invalid token → Redirect to login page
            return new ModelAndView("redirect:http://localhost:8080");
        }
    }

    // --- Doctor Dashboard ---
    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        Map<String, Object> validation = tokenValidationService.validateToken(token, "doctor");

        if (validation.isEmpty()) {
            // Token is valid → Load doctor dashboard
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            // Invalid token → Redirect to login page
            return new ModelAndView("redirect:http://localhost:8080");
        }
    }
}
