package com.f1manager.repository;

import com.f1manager.model.GrandPrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface GrandPrixRepository extends JpaRepository<GrandPrix, Long> {

    List<GrandPrix> findAllByOrderByDataGpAsc();

    @Query("SELECT g FROM GrandPrix g WHERE g.dataGp >= :start AND g.dataGp <= :end ORDER BY g.dataGp ASC")
    List<GrandPrix> findAllByYear(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
