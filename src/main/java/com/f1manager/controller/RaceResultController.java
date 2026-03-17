package com.f1manager.controller;

import com.f1manager.service.DriverService;
import com.f1manager.service.GrandPrixService;
import com.f1manager.service.RaceResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/races")
public class RaceResultController {

    private final GrandPrixService grandPrixService;
    private final DriverService driverService;
    private final RaceResultService raceResultService;

    public RaceResultController(GrandPrixService grandPrixService,
                                 DriverService driverService,
                                 RaceResultService raceResultService) {
        this.grandPrixService = grandPrixService;
        this.driverService = driverService;
        this.raceResultService = raceResultService;
    }

    @GetMapping("/{id}/results")
    public String resultsForm(@PathVariable Long id, Model model) {
        model.addAttribute("race", grandPrixService.findById(id));
        model.addAttribute("drivers", driverService.findAll());
        model.addAttribute("points", raceResultService.getPoints());
        return "races/results";
    }

    @PostMapping("/{id}/results")
    public String submitResults(@PathVariable Long id,
                                @RequestParam List<Long> driverIds,
                                RedirectAttributes redirectAttributes) {
        raceResultService.assignPoints(driverIds);
        redirectAttributes.addFlashAttribute("success", "Punctele au fost asignate!");
        return "redirect:/leaderboard/drivers";
    }
}
