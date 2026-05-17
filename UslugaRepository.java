package org.example.repository;

import org.example.entity.Usluga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UslugaRepository extends JpaRepository<Usluga, Long> {
    List<Usluga> findByNameContainingIgnoreCase(String name);
}