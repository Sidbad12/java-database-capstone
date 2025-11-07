package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    // -------------------- Book Appointment --------------------
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // success
        } catch (Exception e) {
            return 0; // error
        }
    }

    // -------------------- Update Appointment --------------------
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());

        if (existing.isEmpty()) {
            response.put("message", "Appointment not found!");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate doctor
        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
        if (doctor.isEmpty()) {
            response.put("message", "Invalid Doctor ID!");
            return ResponseEntity.badRequest().body(response);
        }

        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully!");
        return ResponseEntity.ok(response);
    }

    // -------------------- Cancel Appointment --------------------
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            response.put("message", "Appointment not found!");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment appointment = appointmentOpt.get();

        // Ensure only the patient who booked it can cancel it
        Map<String, Object> tokenData = tokenService.decodeToken(token);
        Long patientId = Long.valueOf(tokenData.get("id").toString());

        if (!appointment.getPatientId().equals(patientId)) {
            response.put("message", "You are not authorized to cancel this appointment!");
            return ResponseEntity.status(403).body(response);
        }

        appointmentRepository.delete(appointment);
        response.put("message", "Appointment cancelled successfully!");
        return ResponseEntity.ok(response);
    }

    // -------------------- Get Appointments List --------------------
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> tokenData = tokenService.decodeToken(token);
        Long doctorId = Long.valueOf(tokenData.get("id").toString());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        // Filter by patient name if provided
        if (pname != null && !pname.trim().isEmpty()) {
            appointments.removeIf(appt -> {
                Optional<Patient> p = patientRepository.findById(appt.getPatientId());
                return p.isEmpty() || !p.get().getName().toLowerCase().contains(pname.toLowerCase());
            });
        }

        result.put("appointments", appointments);
        return result;
    }
}
