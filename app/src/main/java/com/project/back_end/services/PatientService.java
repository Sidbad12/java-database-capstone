package com.project.back_end.services;

import com.project.back_end.models.Patient;
import com.project.back_end.models.Appointment;
import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    // -----------------------------------------------------
    // 1. Create Patient
    // -----------------------------------------------------
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // -----------------------------------------------------
    // 2. Get Appointments for Logged Patient
    // -----------------------------------------------------
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();

        String emailFromToken = tokenService.getEmailFromToken(token);
        Patient patient = patientRepository.findByEmail(emailFromToken);

        if (patient == null || !Objects.equals(patient.getId(), id)) {
            response.put("message", "Unauthorized Access");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> dtos = appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()
                )).collect(Collectors.toList());

        response.put("appointments", dtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // -----------------------------------------------------
    // 3. Filter by Condition (Past / Future)
    // -----------------------------------------------------
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments;

        if (condition.equalsIgnoreCase("past")) {
            appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, 1);
        } else {
            appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, 0);
        }

        response.put("appointments", convertToDTO(appointments));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // -----------------------------------------------------
    // 4. Filter by Doctor Name
    // -----------------------------------------------------
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();

        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(name, patientId);

        response.put("appointments", convertToDTO(appointments));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // -----------------------------------------------------
    // 5. Filter by Doctor + Condition
    // -----------------------------------------------------
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Map<String, Object> response = new HashMap<>();

        int status = condition.equalsIgnoreCase("past") ? 1 : 0;

        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status);

        response.put("appointments", convertToDTO(appointments));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // -----------------------------------------------------
    // 6. Get Patient Details from Token
    // -----------------------------------------------------
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> response = new HashMap<>();

        String email = tokenService.getEmailFromToken(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            response.put("message", "Patient not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("patient", patient);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // -----------------------------------------------------
    // Helper Method: Convert List to DTO
    // -----------------------------------------------------
    private List<AppointmentDTO> convertToDTO(List<Appointment> appointments) {
        return appointments.stream()
                .map(a -> new AppointmentDTO(
                        a.getId(),
                        a.getDoctor().getId(),
                        a.getDoctor().getName(),
                        a.getPatient().getId(),
                        a.getPatient().getName(),
                        a.getPatient().getEmail(),
                        a.getPatient().getPhone(),
                        a.getPatient().getAddress(),
                        a.getAppointmentTime(),
                        a.getStatus()
                )).collect(Collectors.toList());
    }
}
