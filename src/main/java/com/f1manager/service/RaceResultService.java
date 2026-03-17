package com.f1manager.service;

import com.f1manager.model.Driver;
import com.f1manager.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RaceResultService {

    private static final int[] POINTS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    private final DriverRepository driverRepository;

    public RaceResultService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Transactional
    public void assignPoints(List<Long> driverIds) {
        Set<Long> alreadyAwarded = new HashSet<>();
        for (int i = 0; i < Math.min(driverIds.size(), POINTS.length); i++) {
            Long driverId = driverIds.get(i);
            if (driverId != null && alreadyAwarded.add(driverId)) {
                Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
                driver.setNrPuncte(driver.getNrPuncte() + POINTS[i]);
                driverRepository.save(driver);
            }
        }
    }

    public int[] getPoints() {
        return POINTS;
    }
}
