package com.yourproject.controller;

import com.yourproject.model.Login;
import com.yourproject.model.Patient;
import com.yourproject.service.PatientService;
import com.yourproject.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    // 1. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatientDetails(@PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "patient");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return patientService.getPatientDetails(token);
    }

    // 2. Create a New Patient (Sign Up)
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    // 3. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // 4. Get Patient Appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "patient");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return patientService.getPatientAppointment(id);
    }

    // 5. Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "patient");
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return service.filterPatient(condition, name, token);
    }
}
