package org.example.service;
import org.example.entity.Usluga;
import org.example.repository.UslugaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UslugaService {

    @Autowired
    private UslugaRepository uslugaRepository;

    // Получить все услуги — доступно всем
    public List<Usluga> getAllUslugi() {
        return uslugaRepository.findAll();
    }

    // Получить услугу по ID — доступно всем
    public Usluga getUslugaById(Long id) {
        return uslugaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));
    }

    // Поиск по названию — доступно всем
    public List<Usluga> searchUslugi(String name) {
        return uslugaRepository.findByNameContainingIgnoreCase(name);
    }

    // Создать услугу — только авторизованные
    public Usluga createUsluga(Usluga usluga) {
        return uslugaRepository.save(usluga);
    }

    // Удалить услугу — только авторизованные
    public void deleteUsluga(Long id) {
        uslugaRepository.deleteById(id);
    }
}