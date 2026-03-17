package com.f1manager.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "grand_prix")
public class GrandPrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nume;

    @Column(nullable = false)
    private int distanta;

    @Column(name = "data_gp", nullable = false)
    private LocalDate dataGp;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_of_day", nullable = false)
    private TimeOfDay timeOfDay;

    @Column(name = "is_sprint", nullable = false)
    private boolean sprint;

    public GrandPrix() {}

    public GrandPrix(String nume, int distanta, LocalDate dataGp, TimeOfDay timeOfDay, boolean sprint) {
        this.nume = nume;
        this.distanta = distanta;
        this.dataGp = dataGp;
        this.timeOfDay = timeOfDay;
        this.sprint = sprint;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public int getDistanta() { return distanta; }
    public void setDistanta(int distanta) { this.distanta = distanta; }
    public LocalDate getDataGp() { return dataGp; }
    public void setDataGp(LocalDate dataGp) { this.dataGp = dataGp; }
    public TimeOfDay getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(TimeOfDay timeOfDay) { this.timeOfDay = timeOfDay; }
    public boolean isSprint() { return sprint; }
    public void setSprint(boolean sprint) { this.sprint = sprint; }
}
