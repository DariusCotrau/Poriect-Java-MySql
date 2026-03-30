package com.f1manager.controller;

import com.f1manager.service.DriverService;
import com.f1manager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final DriverService driverService;
    private final TeamService teamService;

    public LeaderboardController(DriverService driverService, TeamService teamService) {
        this.driverService = driverService;
        this.teamService = teamService;
    }

    @GetMapping("/drivers")
    public String driverStandings(@RequestParam(defaultValue = "0") int season, Model model) {
        model.addAttribute("drivers", driverService.findAllSortedByPointsBySeason(season));
        model.addAttribute("selectedSeason", season);
        model.addAttribute("seasons", new int[]{2026, 2025, 2024, 2023, 2022, 2021, 2020});
        return "leaderboard/drivers";
    }

    @GetMapping("/teams")
    public String teamStandings(@RequestParam(defaultValue = "0") int season, Model model) {
        model.addAttribute("teams", teamService.findAllSortedByTotalPointsBySeason(season));
        model.addAttribute("selectedSeason", season);
        model.addAttribute("seasons", new int[]{2026, 2025, 2024, 2023, 2022, 2021, 2020});
        return "leaderboard/teams";
    }
}
