package Proiect;

import java.sql.*;
import java.util.ArrayList;

public class Echipa {
    private String nume;
    private int nrCampionate;
    private ArrayList<Driver> drivers;
    private int nrPodiumuri;

    public Echipa(String nume, int nrCampionate, ArrayList<Driver> drivers, int nrPodiumuri) {
        this.nume = nume;
        this.nrCampionate = nrCampionate;
        this.drivers = drivers;
        this.nrPodiumuri = nrPodiumuri;
    }

    public static ArrayList<Echipa> loadTeamsFromDatabase() {
        ArrayList<Echipa> echipe = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:f1manager.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Teams");
            while (rs.next()) {
                echipe.add(new Echipa(
                        rs.getString("nume"),
                        rs.getInt("nrCampionate"),
                        new ArrayList<>(),
                        rs.getInt("nrPodiumuri")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return echipe;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<Driver> drivers) {
        this.drivers = drivers;
    }

    public void setTotalPoints(int totalPoints) {
        // Metoda corectă pentru setarea punctelor
        int totalPuncte = 0;
        for (Driver driver : drivers) {
            totalPuncte += driver.getNrPuncte();
        }
    }

    public int getTotalPuncte() {
        int totalPuncte = 0;
        for (Driver driver : drivers) {
            totalPuncte += driver.getNrPuncte();
        }
        return totalPuncte;
    }


    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public int getNrCampionate() { return nrCampionate; }
    public void setNrCampionate(int nrCampionate) { this.nrCampionate = nrCampionate; }
    public int getNrPodiumuri() { return nrPodiumuri; }
    public void setNrPodiumuri(int nrPodiumuri) { this.nrPodiumuri = nrPodiumuri; }
}
