package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.dto.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.ServiceMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ServiceMain serviceMain;  // Fixed: use ServiceMain

    // 1. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, user);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity(validationResponse.getBody(), validationResponse.getStatusCode());
        }

        return doctorService.getDoctorAvailability(doctorId, date);
    }

    
    // 4. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // Line 43
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        return doctorService.getDoctors();
    }

    // Line 57
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "admin");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        return doctorService.saveDoctor(doctor);
    }

    // Line 77
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "admin");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        return doctorService.updateDoctor(doctor);
    }

    // Line 91
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = serviceMain.validateToken(token, "admin");
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        return doctorService.deleteDoctor(id);
    }

    // Line 101
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        return serviceMain.filterDoctor(name, speciality, time);
    }

    
}
