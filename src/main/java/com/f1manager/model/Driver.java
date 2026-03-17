package com.f1manager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private int campionate;

    @Column(name = "nr_puncte", nullable = false)
    private int nrPuncte;

    @Column(name = "nr_podium", nullable = false)
    private int nrPodium;

    @Column(name = "nr_castig", nullable = false)
    private int nrCastig;

    public Driver() {}

    public Driver(String nume, Team team, int campionate, int nrPuncte, int nrPodium, int nrCastig) {
        this.nume = nume;
        this.team = team;
        this.campionate = campionate;
        this.nrPuncte = nrPuncte;
        this.nrPodium = nrPodium;
        this.nrCastig = nrCastig;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public int getCampionate() { return campionate; }
    public void setCampionate(int campionate) { this.campionate = campionate; }
    public int getNrPuncte() { return nrPuncte; }
    public void setNrPuncte(int nrPuncte) { this.nrPuncte = nrPuncte; }
    public int getNrPodium() { return nrPodium; }
    public void setNrPodium(int nrPodium) { this.nrPodium = nrPodium; }
    public int getNrCastig() { return nrCastig; }
    public void setNrCastig(int nrCastig) { this.nrCastig = nrCastig; }
}
