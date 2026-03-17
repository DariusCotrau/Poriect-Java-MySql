package com.f1manager.repository;

import com.f1manager.model.GrandPrix;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GrandPrixRepository extends JpaRepository<GrandPrix, Long> {

    List<GrandPrix> findAllByOrderByDataGpAsc();
}
