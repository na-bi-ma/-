package org.example.repository;

import org.example.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndUslugaId(Long userId, Long uslugaId);
    boolean existsByUserIdAndUslugaId(Long userId, Long uslugaId);
    void deleteByUserIdAndUslugaId(Long userId, Long uslugaId);
}