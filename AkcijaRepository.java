package org.example.repository;

import org.example.entity.Akcija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AkcijaRepository extends JpaRepository<Akcija, Long> {
    List<Akcija> findByIsActiveTrue();
}