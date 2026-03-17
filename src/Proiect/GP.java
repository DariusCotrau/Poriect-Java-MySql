package Proiect;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GP {
    private String nume;
    private int distanta;
    private LocalDate dataGP;
    private TimeOfDay timeOfDay;
    private boolean isSprint;

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDistanta(int distanta) {
        this.distanta = distanta;
    }

    public void setDataGP(LocalDate dataGP) {
        this.dataGP = dataGP;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public void setSprint(boolean sprint) {
        isSprint = sprint;
    }

    public GP(String nume, int distanta, LocalDate dataGP, TimeOfDay timeOfDay, boolean isSprint) {
        this.nume = nume;
        this.distanta = distanta;
        this.dataGP = dataGP;
        this.timeOfDay = timeOfDay;
        this.isSprint = isSprint;
    }

    public static ArrayList<GP> loadRacesFromDatabase() {
        ArrayList<GP> races = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:f1manager.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Races");
            while (rs.next()) {
                races.add(new GP(
                        rs.getString("nume"),
                        rs.getInt("distanta"),
                        LocalDate.parse(rs.getString("dataGP")),
                        TimeOfDay.valueOf(rs.getString("timeOfDay")),
                        rs.getBoolean("isSprint")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return races;
    }

    public String getNume() {
        return nume;
    }

    public int getDistanta() {
        return distanta;
    }

    public LocalDate getDataGP() {
        return dataGP;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public boolean isSprint() {
        return isSprint;
    }
}
