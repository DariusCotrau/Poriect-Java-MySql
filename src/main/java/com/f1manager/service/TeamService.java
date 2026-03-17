package com.f1manager.service;

import com.f1manager.model.Team;
import com.f1manager.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Team not found: " + id));
    }

    @Transactional
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Transactional
    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public List<Team> findAllSortedByTotalPoints() {
        List<Team> teams = teamRepository.findAllWithDrivers();
        teams.sort(Comparator.comparingInt(Team::getTotalPuncte).reversed());
        return teams;
    }
}
