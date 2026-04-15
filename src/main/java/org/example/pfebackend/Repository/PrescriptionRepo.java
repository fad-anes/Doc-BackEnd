package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription, Integer>{
    public List<Prescription> findByPatient_Id(Integer id);
    public List<Prescription> findByDoctor_Id(Integer id);
    public List<Prescription> findByPharmacy_Id(Integer id);
}
