package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayOffRepo extends JpaRepository<DayOff, Integer> {
    public boolean findByDateOffAndDoctor_Id(LocalDate dateOff, Integer id);
    public List<DayOff> findByDoctor_Id(Integer id);
}
