package com.f1manager.controller;

import com.f1manager.model.Team;
import com.f1manager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamService.findAllSortedByTotalPoints());
        return "teams/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("team", new Team());
        return "teams/form";
    }

    @PostMapping
    public String save(@ModelAttribute Team team) {
        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("team", teamService.findById(id));
        return "teams/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Team team) {
        team.setId(id);
        teamService.save(team);
        return "redirect:/teams";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        teamService.delete(id);
        return "redirect:/teams";
    }
}
