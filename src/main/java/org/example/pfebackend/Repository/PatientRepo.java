package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer>{
    public Optional<Patient> findByEmail(String email);
}
