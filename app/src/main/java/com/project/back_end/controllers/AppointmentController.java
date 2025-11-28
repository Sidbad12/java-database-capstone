package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.ServiceMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

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

        // Validate token for Patient
        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (validationResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired token"));
        }

        // Validate Appointment Slot
        int validation = serviceMain.validateAppointment(appointment);
        if (validation == -1) {
            return ResponseEntity.badRequest().body(Map.of("message", "Doctor does not exist"));
        }
        if (validation == 0) {
            return ResponseEntity.status(409).body(Map.of("message", "Time slot not available"));
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return ResponseEntity.status(201).body(Map.of("message", "Appointment booked successfully"));
        }

        return ResponseEntity.status(500).body(Map.of("message", "Failed to book appointment"));
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
