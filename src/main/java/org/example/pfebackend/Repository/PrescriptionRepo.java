package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription, Integer>{
}
