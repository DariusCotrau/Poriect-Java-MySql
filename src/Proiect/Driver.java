package Proiect;

import java.sql.*;
import java.util.ArrayList;

public class Driver {
    private String nume;
    private String numeEchipa;
    private int campionate;
    private int nrPuncte;
    private int nrPodium;
    private int nrCastig;
    private int id;

    public Driver(int id, String nume, String numeEchipa, int campionate, int nrPuncte, int nrPodium, int nrCastig) {
        this.id = id;
        this.nume = nume;
        this.numeEchipa = numeEchipa;
        this.campionate = campionate;
        this.nrPuncte = nrPuncte;
        this.nrPodium = nrPodium;
        this.nrCastig = nrCastig;
    }

    public static ArrayList<Driver> loadDriversFromDatabase() {
        ArrayList<Driver> drivers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:f1manager.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Drivers");
            while (rs.next()) {
                drivers.add(new Driver(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("numeEchipa"),
                        rs.getInt("campionate"),
                        rs.getInt("nrPuncte"),
                        rs.getInt("nrPodium"),
                        rs.getInt("nrCastig")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // Getteri și setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getNumeEchipa() { return numeEchipa; }
    public void setNumeEchipa(String numeEchipa) { this.numeEchipa = numeEchipa; }
    public int getCampionate() { return campionate; }
    public void setCampionate(int campionate) { this.campionate = campionate; }
    public int getNrPuncte() { return nrPuncte; }
    public void setNrPuncte(int nrPuncte) { this.nrPuncte = nrPuncte; }
    public int getNrPodium() { return nrPodium; }
    public void setNrPodium(int nrPodium) { this.nrPodium = nrPodium; }
    public int getNrCastig() { return nrCastig; }
    public void setNrCastig(int nrCastig) { this.nrCastig = nrCastig; }
}