package org.example.service;

import org.example.entity.Favorite;
import org.example.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public boolean isFavorite(Long userId, Long uslugaId) {
        return favoriteRepository.existsByUserIdAndUslugaId(userId, uslugaId);
    }

    public Favorite addFavorite(Long userId, Long uslugaId) {
        if (favoriteRepository.existsByUserIdAndUslugaId(userId, uslugaId)) {
            throw new RuntimeException("Уже в избранном");
        }
        Favorite favorite = new Favorite(userId, uslugaId);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long userId, Long uslugaId) {
        favoriteRepository.deleteByUserIdAndUslugaId(userId, uslugaId);
    }
}