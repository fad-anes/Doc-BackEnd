package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Integer>{
}
