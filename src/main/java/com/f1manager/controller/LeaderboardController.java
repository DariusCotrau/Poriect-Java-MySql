package com.f1manager.controller;

import com.f1manager.service.DriverService;
import com.f1manager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String driverStandings(Model model) {
        model.addAttribute("drivers", driverService.findAllSortedByPoints());
        return "leaderboard/drivers";
    }

    @GetMapping("/teams")
    public String teamStandings(Model model) {
        model.addAttribute("teams", teamService.findAllSortedByTotalPoints());
        return "leaderboard/teams";
    }
}
