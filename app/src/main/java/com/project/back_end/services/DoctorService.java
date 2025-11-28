package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.dto.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    // ================= Get Doctor Availability (returns List) =================
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // Example available time slots
        List<String> allSlots = Arrays.asList("09:00 AM", "10:00 AM", "11:00 AM", "03:00 PM", "04:00 PM", "05:00 PM");

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> booked = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        // Remove booked slots
        List<String> bookedSlots = new ArrayList<>();
        for (Appointment a : booked) {
            bookedSlots.add(a.getAppointmentTime().toLocalTime().toString());
        }

        List<String> availableSlots = new ArrayList<>(allSlots);
        availableSlots.removeAll(bookedSlots);
        return availableSlots;
    }

    // ================= Get Doctor Availability (returns ResponseEntity) =================
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(Long doctorId, String dateString) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            LocalDate date = LocalDate.parse(dateString);
            List<String> availableSlots = getDoctorAvailability(doctorId, date);
            
            response.put("availableSlots", availableSlots);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error retrieving availability");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= Save Doctor =================
    public ResponseEntity<Map<String, String>> saveDoctor(Doctor doctor) {
        Map<String, String> response = new HashMap<>();
        
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) {
                response.put("message", "Doctor already exists");
                return ResponseEntity.badRequest().body(response);
            }
            doctorRepository.save(doctor);
            response.put("message", "Doctor saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Error saving doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= Update Doctor =================
    public ResponseEntity<Map<String, String>> updateDoctor(Doctor doctor) {
        Map<String, String> response = new HashMap<>();
        
        Optional<Doctor> existing = doctorRepository.findById(doctor.getId());
        if (existing.isEmpty()) {
            response.put("message", "Doctor not found");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            doctorRepository.save(doctor);
            response.put("message", "Doctor updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error updating doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= Get All Doctors =================
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorRepository.findAll());
        return ResponseEntity.ok(response);
    }

    // ================= Delete Doctor =================
    public ResponseEntity<Map<String, String>> deleteDoctor(long id) {
        Map<String, String> response = new HashMap<>();
        
        Optional<Doctor> doc = doctorRepository.findById(id);
        if (doc.isEmpty()) {
            response.put("message", "Doctor not found");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error deleting doctor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================= Validate Doctor Login =================
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();

        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid Credentials");
            return ResponseEntity.badRequest().body(response);
        }

        String token = tokenService.generateToken(String.valueOf(doctor.getId()), "doctor");

        response.put("message", "Login Successful");
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // ================= Find Doctor By Name =================
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctorRepository.findByNameContainingIgnoreCase(name));
        return result;
    }

    // ================= Filter Doctors By Name, Specialty, Time =================
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);

        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCase(name);
        doctors = filterDoctorByTime(doctors, amOrPm);

        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty));
        return result;
    }

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);

        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctorRepository.findBySpecialtyIgnoreCase(specialty));
        return result;
    }

    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);

        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

    // ================= Private Time Filter =================
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        List<Doctor> filtered = new ArrayList<>();
        for (Doctor d : doctors) {
            // Fixed: Changed from getAvailableTime() to getAvailableTimes()
            if (d.getAvailableTimes() != null && !d.getAvailableTimes().isEmpty()) {
                // Check if any available time matches the AM/PM preference
                boolean matches = d.getAvailableTimes().stream()
                    .anyMatch(time -> {
                        try {
                            LocalTime t = LocalTime.parse(time);
                            return (amOrPm.equalsIgnoreCase("AM") && t.isBefore(LocalTime.NOON)) ||
                                   (amOrPm.equalsIgnoreCase("PM") && !t.isBefore(LocalTime.NOON));
                        } catch (Exception e) {
                            // If time format doesn't parse, try direct string comparison
                            return time.toUpperCase().contains(amOrPm.toUpperCase());
                        }
                    });
                if (matches) {
                    filtered.add(d);
                }
            }
        }
        return filtered;
    }
}