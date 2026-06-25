package org.example.repository;

import org.example.entity.Vrach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VrachRepository extends JpaRepository<Vrach, Long> {
    List<Vrach> findByFullNameContainingIgnoreCase(String fullName);
}