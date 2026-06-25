package org.example.service;

import org.example.entity.Akcija;
import org.example.repository.AkcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AkcijaService {

    @Autowired
    private AkcijaRepository akcijaRepository;

    public List<Akcija> getActiveAkcii() {
        return akcijaRepository.findByIsActiveTrue();
    }

    public List<Akcija> getAllAkcii() {
        return akcijaRepository.findAll();
    }

    public Akcija createAkcija(Akcija akcija) {
        return akcijaRepository.save(akcija);
    }

    public void deleteAkcija(Long id) {
        akcijaRepository.deleteById(id);
    }
}