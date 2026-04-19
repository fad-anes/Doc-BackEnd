package org.example.pfebackend.Repository;

import org.example.pfebackend.Entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Integer> {
    Optional<Otp> findByIdUserAndType(Integer id, String type);
}
