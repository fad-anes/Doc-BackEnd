package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.MedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalTestRepo extends JpaRepository<MedicalTest, Integer> {
    public List<MedicalTest> findByPatient_Id(Integer id);
    public List<MedicalTest> findByDoctor_Id(Integer id);
    public List<MedicalTest> findByLaboratory_Id(Integer id);
}
