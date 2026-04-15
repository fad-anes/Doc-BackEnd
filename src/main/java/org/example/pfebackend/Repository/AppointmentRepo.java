package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Integer>{
    public Optional<Appointment> findByDateAndDoctor_Id(LocalDateTime date, Integer id);
    public Optional<Appointment> findByDateAndPatient_Id(LocalDateTime date, Integer id);
    public List<Appointment> findByDoctor_Id(Integer id);
    public List<Appointment> findByPatient_Id(Integer id);
}
