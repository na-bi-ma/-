package org.example.service;

import org.example.entity.Vrach;
import org.example.repository.VrachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VrachService {

    @Autowired
    private VrachRepository vrachRepository;

    public List<Vrach> getAllVrachi() {
        return vrachRepository.findAll();
    }

    public List<Vrach> searchVrachi(String query) {
        return vrachRepository.findByFullNameContainingIgnoreCase(query);
    }

    public Vrach getVrachById(Long id) {
        return vrachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Врач не найден"));
    }

    public Vrach createVrach(Vrach vrach) {
        return vrachRepository.save(vrach);
    }

    public void deleteVrach(Long id) {
        vrachRepository.deleteById(id);
    }
}