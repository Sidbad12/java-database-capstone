package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.ServiceMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ServiceMain serviceMain;  // Use this for all service calls

    // ✅ Get Appointments (Doctor Side)
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {

        // Validate token for Doctor
        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "doctor");
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired token"));
        }

        LocalDate appointmentDate = LocalDate.parse(date);

        Map<String, Object> response = appointmentService.getAppointment(patientName, appointmentDate, token);
        return ResponseEntity.ok(response);
    }

    // ✅ Book Appointment (Patient Side)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return validationResponse;
        }

        ResponseEntity<Map<String, Object>> appointmentValidation = serviceMain.validateAppointment(appointment);
        
        if (appointmentValidation.getBody() != null) {
            Integer status = (Integer) appointmentValidation.getBody().get("status");
            if (status != null && status == -1) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Doctor does not exist");
                return ResponseEntity.badRequest().body(response);
            }
            if (status != null && status == 0) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Time slot not available");
                return ResponseEntity.status(409).body(response);
            }
        }

        int result = appointmentService.bookAppointment(appointment);
        Map<String, String> response = new HashMap<>();
        if (result == 1) {
            response.put("message", "Appointment booked successfully");
            return ResponseEntity.status(201).body(response);
        }

        response.put("message", "Failed to book appointment");
        return ResponseEntity.status(500).body(response);
    }

    // ✅ Update Appointment (Patient Side)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired token"));
        }

        return appointmentService.updateAppointment(appointment);
    }

    // ✅ Cancel Appointment (Patient Side)
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired token"));
        }

        return appointmentService.cancelAppointment(id, token);
    }
}
