package com.f1manager.repository;

import com.f1manager.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT d FROM Driver d JOIN FETCH d.team ORDER BY d.nrPuncte DESC")
    List<Driver> findAllWithTeamOrderByNrPuncteDesc();

    @Query("SELECT d FROM Driver d JOIN FETCH d.team")
    List<Driver> findAllWithTeam();

    @Query("SELECT d FROM Driver d JOIN FETCH d.team WHERE d.season = :season ORDER BY d.nrPuncte DESC")
    List<Driver> findAllWithTeamBySeason(@Param("season") int season);

    @Query("SELECT d FROM Driver d JOIN FETCH d.team WHERE d.season = :season")
    List<Driver> findAllWithTeamOnlyBySeason(@Param("season") int season);
}
