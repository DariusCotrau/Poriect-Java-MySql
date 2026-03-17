package Proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CurseTab extends JPanel {

    private JTable curseTable;
    private ArrayList<GP> curse;
    private ArrayList<Driver> drivers;

    public CurseTab(ArrayList<GP> curse, ArrayList<Driver> drivers) {
        this.curse = curse;
        this.drivers = drivers;

        setLayout(new BorderLayout());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String[] columnNames = {"Nume Circuit", "Distanta (km)", "Data Curse", "Momentul din zi", "Sprint"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (GP gp : curse) {
            Object[] row = {
                    gp.getNume(),
                    gp.getDistanta(),
                    gp.getDataGP().format(formatter),
                    gp.getTimeOfDay(),
                    gp.isSprint() ? "Da" : "Nu"
            };
            tableModel.addRow(row);
        }

        curseTable = new JTable(tableModel);

        curseTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = curseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        GP selectedGP = curse.get(selectedRow);
                        openRaceDialog(selectedGP);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(curseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void openRaceDialog(GP selectedGP) {
        RaceResultsDialog dialog = new RaceResultsDialog((Frame) SwingUtilities.getWindowAncestor(this), selectedGP, drivers);
        dialog.setVisible(true);
    }
}
