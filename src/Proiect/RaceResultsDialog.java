package Proiect;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RaceResultsDialog extends JDialog {

    private JComboBox<String>[] positionComboBoxes;
    private JButton confirmButton;
    private ArrayList<Driver> drivers;

    private final int[] POINTS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    public JComboBox<String>[] getPositionComboBoxes() {
        return positionComboBoxes;
    }

    public RaceResultsDialog(Frame owner, GP selectedGP, ArrayList<Driver> drivers) {
        super(owner, "Rezultate Cursa: " + selectedGP.getNume(), true);
        this.drivers = new ArrayList<>(drivers);

        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(owner);

        JLabel titleLabel = new JLabel("Selectează Driverii pentru Cursa " + selectedGP.getNume());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel positionsPanel = new JPanel();
        positionsPanel.setLayout(new GridLayout(11, 2, 10, 10));

        positionComboBoxes = new JComboBox[10];

        for (int i = 0; i < 10; i++) {
            JLabel label = new JLabel("Poziția " + (i + 1) + " (" + POINTS[i] + " pct):");
            positionComboBoxes[i] = new JComboBox<>();
            for (Driver driver : drivers) {
                positionComboBoxes[i].addItem(driver.getNume());
            }
            positionsPanel.add(label);
            positionsPanel.add(positionComboBoxes[i]);
        }

        add(positionsPanel, BorderLayout.CENTER);

        confirmButton = new JButton("Confirmă Rezultatele");
        confirmButton.addActionListener(e -> assignPoints());
        add(confirmButton, BorderLayout.SOUTH);
    }

    void assignPoints() {
        ArrayList<String> selectedDrivers = new ArrayList<>();

        for (int i = 0; i < positionComboBoxes.length; i++) {
            String selectedDriverName = (String) positionComboBoxes[i].getSelectedItem();

            if (selectedDriverName != null && !selectedDrivers.contains(selectedDriverName)) {
                selectedDrivers.add(selectedDriverName);
                for (Driver driver : drivers) {
                    if (driver.getNume().equals(selectedDriverName)) {
                        driver.setNrPuncte(driver.getNrPuncte() + POINTS[i]);
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Punctele au fost asignate!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
