package com.f1manager.f1live;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/f1")
public class F1LiveController {

    private final F1DataService f1DataService;

    public F1LiveController(F1DataService f1DataService) {
        this.f1DataService = f1DataService;
    }

    @GetMapping("/calendar")
    public String calendar(@RequestParam(defaultValue = "0") int year, Model model) {
        if (year == 0) year = LocalDate.now().getYear();
        Map<String, Object> data = f1DataService.getSchedule(year);
        model.addAttribute("data", data);
        model.addAttribute("selectedYear", year);
        model.addAttribute("years", new int[]{2026, 2025, 2024, 2023, 2022, 2021, 2020});
        return "f1live/calendar";
    }

    @GetMapping("/session")
    public String session(
            @RequestParam int year,
            @RequestParam int round,
            @RequestParam(defaultValue = "R") String sessionType,
            Model model) {
        Map<String, Object> data = f1DataService.getResults(year, round, sessionType);
        model.addAttribute("data", data);
        model.addAttribute("year", year);
        model.addAttribute("round", round);
        model.addAttribute("sessionType", sessionType);
        return "f1live/session";
    }

    @GetMapping("/telemetry")
    public String telemetry(
            @RequestParam int year,
            @RequestParam int round,
            @RequestParam(defaultValue = "R") String sessionType,
            @RequestParam(required = false) String driver,
            @RequestParam(required = false) String driver2,
            Model model) {
        Map<String, Object> driversData = f1DataService.getSessionDrivers(year, round, sessionType);
        model.addAttribute("driversData", driversData);
        model.addAttribute("year", year);
        model.addAttribute("round", round);
        model.addAttribute("sessionType", sessionType);
        model.addAttribute("selectedDriver", driver != null ? driver : "");
        model.addAttribute("selectedDriver2", driver2 != null ? driver2 : "");

        if (driver != null && !driver.isBlank()) {
            Map<String, Object> telemetry = f1DataService.getTelemetry(year, round, sessionType, driver, driver2);
            model.addAttribute("telemetryData", telemetry);
        }
        return "f1live/telemetry";
    }

    @GetMapping("/fastest-laps")
    public String fastestLaps(
            @RequestParam int year,
            @RequestParam int round,
            Model model) {
        Map<String, Object> data = f1DataService.getFastestLaps(year, round);
        model.addAttribute("data", data);
        model.addAttribute("year", year);
        model.addAttribute("round", round);
        return "f1live/fastest-laps";
    }
}
