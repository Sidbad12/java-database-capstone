package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    // ================= Get Doctor Availability =================
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

    // ================= Save Doctor =================
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) {
                return -1; // Doctor already exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    // ================= Update Doctor =================
    public int updateDoctor(Doctor doctor) {
        Optional<Doctor> existing = doctorRepository.findById(doctor.getId());
        if (existing.isEmpty()) {
            return -1; // Not found
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ================= Get All Doctors =================
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // ================= Delete Doctor =================
    public int deleteDoctor(long id) {
        Optional<Doctor> doc = doctorRepository.findById(id);
        if (doc.isEmpty()) return -1;

        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
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

        String token = tokenService.generateToken(doctor.getId(), "doctor");

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
            if (d.getAvailableTime().equalsIgnoreCase(amOrPm)) {
                filtered.add(d);
            }
        }
        return filtered;
    }
}
