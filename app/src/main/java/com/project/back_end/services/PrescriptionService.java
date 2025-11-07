package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // 1. Save Prescription
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();

        try {
            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("message", "Error saving prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Get Prescription by Appointment ID
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId);

            response.put("prescription", prescription);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error retrieving prescription");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
