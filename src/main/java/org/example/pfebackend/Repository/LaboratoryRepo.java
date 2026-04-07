package org.example.pfebackend.Repository;

import org.example.pfebackend.Entity.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaboratoryRepo extends JpaRepository<Laboratory, Integer>{
    public Optional<Laboratory> findByEmail(String email);
}
