package com.f1manager.controller;

import com.f1manager.model.Driver;
import com.f1manager.service.DriverService;
import com.f1manager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;
    private final TeamService teamService;

    public DriverController(DriverService driverService, TeamService teamService) {
        this.driverService = driverService;
        this.teamService = teamService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("drivers", driverService.findAll());
        return "drivers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("driver", new Driver());
        model.addAttribute("teams", teamService.findAll());
        return "drivers/form";
    }

    @PostMapping
    public String save(@ModelAttribute Driver driver, @RequestParam Long teamId) {
        driver.setTeam(teamService.findById(teamId));
        driverService.save(driver);
        return "redirect:/drivers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("driver", driverService.findById(id));
        model.addAttribute("teams", teamService.findAll());
        return "drivers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Driver driver, @RequestParam Long teamId) {
        driver.setId(id);
        driver.setTeam(teamService.findById(teamId));
        driverService.save(driver);
        return "redirect:/drivers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        driverService.delete(id);
        return "redirect:/drivers";
    }
}
