package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find Patient by email
    Patient findByEmail(String email);

    // Find Patient by either email or phone
    Patient findByEmailOrPhone(String email, String phone);
}
