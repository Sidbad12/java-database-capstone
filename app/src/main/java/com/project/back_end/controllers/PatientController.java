package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.ServiceMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ServiceMain serviceMain;  // fixed

    // 1. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatientDetails(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return patientService.getPatientDetails(token);
    }

    // 2. Create a New Patient (Sign Up)
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();
        
        int result = patientService.createPatient(patient);
        
        if (result == 1) {
            response.put("message", "Patient created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to create patient");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 3. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return serviceMain.validatePatientLogin(login);
    }

    // 4. Get Patient Appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return patientService.getPatientAppointment(id, token);
    }

    // 5. Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "patient");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return serviceMain.filterPatient(condition, name, token);
    }
}
