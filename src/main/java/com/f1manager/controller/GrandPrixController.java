package com.f1manager.controller;

import com.f1manager.model.GrandPrix;
import com.f1manager.service.GrandPrixService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/races")
public class GrandPrixController {

    private final GrandPrixService grandPrixService;

    public GrandPrixController(GrandPrixService grandPrixService) {
        this.grandPrixService = grandPrixService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int season, Model model) {
        model.addAttribute("races", grandPrixService.findAllByYear(season));
        model.addAttribute("selectedSeason", season);
        model.addAttribute("seasons", new int[]{2026, 2025, 2024, 2023, 2022, 2021, 2020});
        return "races/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("race", new GrandPrix());
        return "races/form";
    }

    @PostMapping
    public String save(@ModelAttribute GrandPrix race) {
        grandPrixService.save(race);
        return "redirect:/races";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("race", grandPrixService.findById(id));
        return "races/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute GrandPrix race) {
        race.setId(id);
        grandPrixService.save(race);
        return "redirect:/races";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        grandPrixService.delete(id);
        return "redirect:/races";
    }
}
