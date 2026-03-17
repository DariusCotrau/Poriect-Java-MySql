package com.f1manager.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nume;

    @Column(name = "nr_campionate", nullable = false)
    private int nrCampionate;

    @Column(name = "nr_podiumuri", nullable = false)
    private int nrPodiumuri;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Driver> drivers = new ArrayList<>();

    public Team() {}

    public Team(String nume, int nrCampionate, int nrPodiumuri) {
        this.nume = nume;
        this.nrCampionate = nrCampionate;
        this.nrPodiumuri = nrPodiumuri;
    }

    @Transient
    public int getTotalPuncte() {
        return drivers.stream().mapToInt(Driver::getNrPuncte).sum();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public int getNrCampionate() { return nrCampionate; }
    public void setNrCampionate(int nrCampionate) { this.nrCampionate = nrCampionate; }
    public int getNrPodiumuri() { return nrPodiumuri; }
    public void setNrPodiumuri(int nrPodiumuri) { this.nrPodiumuri = nrPodiumuri; }
    public List<Driver> getDrivers() { return drivers; }
    public void setDrivers(List<Driver> drivers) { this.drivers = drivers; }
}
