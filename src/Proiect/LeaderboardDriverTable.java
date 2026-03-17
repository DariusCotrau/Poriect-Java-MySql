package Proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardDriverTable {

    private ArrayList<Driver> drivers;
    private JTable leaderboardTable;

    public LeaderboardDriverTable(ArrayList<Driver> drivers) {
        this.drivers = drivers;
        leaderboardTable = new JTable();
        updateLeaderboard(drivers);
    }

    public JPanel getLeaderboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.add(new JScrollPane(leaderboardTable), BorderLayout.CENTER);
        return panel;
    }

    public void updateLeaderboard(ArrayList<Driver> drivers) {
        Collections.sort(drivers, new Comparator<Driver>() {
            @Override
            public int compare(Driver d1, Driver d2) {
                return Integer.compare(d2.getNrPuncte(), d1.getNrPuncte());
            }
        });

        String[] columnNames = {"Nume", "Echipă", "Puncte"};
        DefaultTableModel leaderboardTableModel = new DefaultTableModel(columnNames, 0);

        for (Driver driver : drivers) {
            Object[] row = {driver.getNume(), driver.getNumeEchipa(), driver.getNrPuncte()};
            leaderboardTableModel.addRow(row);
        }

        leaderboardTable.setModel(leaderboardTableModel);

        // Personalizare tabel leaderboard
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 12));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setSelectionBackground(new Color(100, 149, 237));
        leaderboardTable.setSelectionForeground(Color.WHITE);
        leaderboardTable.setGridColor(Color.LIGHT_GRAY);
        leaderboardTable.setShowGrid(true);
    }
}
