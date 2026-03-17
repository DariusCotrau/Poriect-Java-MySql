package com.f1manager.service;

import com.f1manager.model.Driver;
import com.f1manager.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> findAll() {
        return driverRepository.findAllWithTeam();
    }

    public Driver findById(Long id) {
        return driverRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Driver not found: " + id));
    }

    @Transactional
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    @Transactional
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

    public List<Driver> findAllSortedByPoints() {
        return driverRepository.findAllWithTeamOrderByNrPuncteDesc();
    }
}
