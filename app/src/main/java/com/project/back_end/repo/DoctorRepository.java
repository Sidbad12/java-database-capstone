package com.project.back_end.repos;
import com.project.back_end.models.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // 1. Find doctor by email
    Doctor findByEmail(String email);

    // 2. Find doctors by partial name match
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameLike(String name);

    // 3. Filter doctors by partial name & specialty (case-insensitive)
    @Query("SELECT d FROM Doctor d " +
           "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    // 4. Find doctors by specialty only (case-insensitive)
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}
