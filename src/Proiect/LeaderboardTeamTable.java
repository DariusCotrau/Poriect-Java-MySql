package Proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardTeamTable {

    private ArrayList<Echipa> echipe;
    private JTable leaderboardTable;

    public LeaderboardTeamTable(ArrayList<Echipa> echipe) {
        this.echipe = echipe;
        leaderboardTable = new JTable();
        updateLeaderboard(echipe);
    }

    public JPanel getLeaderboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.add(new JScrollPane(leaderboardTable), BorderLayout.CENTER);
        return panel;
    }

    public void updateLeaderboard(ArrayList<Echipa> echipe) {
        Collections.sort(echipe, new Comparator<Echipa>() {
            @Override
            public int compare(Echipa e1, Echipa e2) {
                return Integer.compare(e2.getTotalPuncte(), e1.getTotalPuncte());
            }
        });

        String[] columnNames = {"Echipă", "Puncte"};
        DefaultTableModel leaderboardTableModel = new DefaultTableModel(columnNames, 0);

        for (Echipa echipa : echipe) {
            Object[] row = {echipa.getNume(), echipa.getTotalPuncte()};
            leaderboardTableModel.addRow(row);
        }

        leaderboardTable.setModel(leaderboardTableModel);

        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 12));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setSelectionBackground(new Color(100, 149, 237));
        leaderboardTable.setSelectionForeground(Color.WHITE);
        leaderboardTable.setGridColor(Color.LIGHT_GRAY);
        leaderboardTable.setShowGrid(true);
    }
}
