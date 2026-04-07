package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacyRepo extends JpaRepository<Pharmacy, Integer>{
    public Optional<Pharmacy> findByEmail(String email);
}
