package com.f1manager.repository;

import com.f1manager.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.drivers")
    List<Team> findAllWithDrivers();

    @Query("SELECT DISTINCT t FROM Team t JOIN FETCH t.drivers d WHERE d.season = :season")
    List<Team> findAllWithDriversBySeason(@Param("season") int season);
}
