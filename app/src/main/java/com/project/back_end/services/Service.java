package com.project.back_end.services;

import com.project.back_end.models.*;
import com.project.back_end.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceMain {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public ServiceMain(TokenService tokenService,
                       AdminRepository adminRepository,
                       DoctorRepository doctorRepository,
                       PatientRepository patientRepository,
                       DoctorService doctorService,
                       PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 1. Validate Token
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();

        if (!tokenService.validateToken(token, user)) {
            response.put("message", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("message", "Token valid");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 2. Admin Login
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();

        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
            response.put("message", "Invalid Credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = tokenService.generateToken(admin.getUsername(), "admin");
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 3. Filter Doctors
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();

        response.put("doctors",
                doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time));

        return response;
    }

    // 4. Validate Appointment Availability
    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(appointment.getDoctorId());

        if (!doctorOptional.isPresent()) return -1;

        Doctor doctor = doctorOptional.get();

        boolean available = doctorService.getDoctorAvailability(doctor, appointment.getTime());
        return available ? 1 : 0;
    }

    // 5. Validate Patient before Registration
    public boolean validatePatient(Patient patient) {
        Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing == null; // true means new (valid), false means duplicate
    }

    // 6. Patient Login
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();

        Patient patient = patientRepository.findByEmail(login.getEmail());

        if (patient == null || !patient.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid Credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = tokenService.generateToken(patient.getEmail(), "patient");
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 7. Filter Patient Appointments
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long patientId = patient.getId();

        if (!condition.isEmpty() && !name.isEmpty())
            return patientService.filterByDoctorAndCondition(condition, name, patientId);
        else if (!condition.isEmpty())
            return patientService.filterByCondition(condition, patientId);
        else
            return patientService.filterByDoctor(name, patientId);
    }
}
