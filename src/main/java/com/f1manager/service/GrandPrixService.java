package com.f1manager.service;

import com.f1manager.model.GrandPrix;
import com.f1manager.repository.GrandPrixRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GrandPrixService {

    private final GrandPrixRepository grandPrixRepository;

    public GrandPrixService(GrandPrixRepository grandPrixRepository) {
        this.grandPrixRepository = grandPrixRepository;
    }

    public List<GrandPrix> findAll() {
        return grandPrixRepository.findAllByOrderByDataGpAsc();
    }

    public GrandPrix findById(Long id) {
        return grandPrixRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Grand Prix not found: " + id));
    }

    @Transactional
    public GrandPrix save(GrandPrix gp) {
        return grandPrixRepository.save(gp);
    }

    @Transactional
    public void delete(Long id) {
        grandPrixRepository.deleteById(id);
    }
}
