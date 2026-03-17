package Proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class MainFrame extends JFrame {
    private ArrayList<Driver> drivers;
    private ArrayList<Echipa> echipe;
    private ArrayList<GP> curse;
    private JTable driverTable;
    private JTable echipaTable;
    private JTable curseTable;

    public MainFrame() {
        setTitle("F1 Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        drivers = Driver.loadDriversFromDatabase();
        echipe = Echipa.loadTeamsFromDatabase();
        curse = GP.loadRacesFromDatabase();

        driverTable = new JTable();
        echipaTable = new JTable();
        curseTable = new JTable();

        initDriverTable();
        initEchipaTable();
        initCurseTable();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(Color.DARK_GRAY);
        tabbedPane.setForeground(Color.WHITE);

        JPanel driverPanel = new JPanel(new BorderLayout());
        driverPanel.add(new JScrollPane(driverTable), BorderLayout.CENTER);

        JPanel echipaPanel = new JPanel(new BorderLayout());
        echipaPanel.add(new JScrollPane(echipaTable), BorderLayout.CENTER);

        JPanel cursePanel = new JPanel(new BorderLayout());
        cursePanel.add(new JScrollPane(curseTable), BorderLayout.CENTER);

        tabbedPane.addTab("Driveri", driverPanel);
        tabbedPane.addTab("Echipe", echipaPanel);
        tabbedPane.addTab("Curse", cursePanel);
        tabbedPane.addTab("Campionatul Driverilor", createSortedDriverTable());
        tabbedPane.addTab("Campionatul Echipe", createSortedTeamTable());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane createSortedDriverTable() {
        String[] columnNames = {"Nume", "Echipa", "Puncte"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        drivers.sort(Comparator.comparingInt(Driver::getNrPuncte).reversed());

        for (Driver driver : drivers) {
            Object[] row = {
                    driver.getNume(),
                    driver.getNumeEchipa(),
                    driver.getNrPuncte()
            };
            model.addRow(row);
        }

        JTable sortedDriverTable = new JTable(model);
        return new JScrollPane(sortedDriverTable);
    }

    private JScrollPane createSortedTeamTable() {
        String[] columnNames = {"Echipa", "Puncte Totale"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        echipe.sort(Comparator.comparingInt(Echipa::getTotalPuncte).reversed());

        for (Echipa echipa : echipe) {
            Object[] row = {
                    echipa.getNume(),
                    echipa.getTotalPuncte()
            };
            model.addRow(row);
        }

        JTable sortedTeamTable = new JTable(model);
        return new JScrollPane(sortedTeamTable);
    }

    private void initDriverTable() {
        String[] columnNames = {"Nume", "Echipa", "Campionate", "Puncte", "Podiumuri", "Câștiguri"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Driver driver : drivers) {
            Object[] row = {
                    driver.getNume(),
                    driver.getNumeEchipa(),
                    driver.getCampionate(),
                    driver.getNrPuncte(),
                    driver.getNrPodium(),
                    driver.getNrCastig()
            };
            model.addRow(row);
        }
        driverTable.setModel(model);
    }

    private void initEchipaTable() {
        String[] columnNames = {"Echipa", "Campionate", "Puncte Totale"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Echipa echipa : echipe) {
            Object[] row = {
                    echipa.getNume(),
                    echipa.getNrCampionate(),
                    echipa.getTotalPuncte()
            };
            model.addRow(row);
        }
        echipaTable.setModel(model);
    }

    private void initCurseTable() {
        String[] columnNames = {"Circuit", "Data", "Distanta"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        curse.sort(Comparator.comparing(GP::getDataGP));

        for (GP cursa : curse) {
            Object[] row = {
                    cursa.getNume(),
                    cursa.getDataGP().toString(),
                    cursa.getDistanta()
            };
            model.addRow(row);
        }
        curseTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
