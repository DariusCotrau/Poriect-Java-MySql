package Proiect;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DriverEditor extends JDialog {
    private Driver driver;
    private JTextField nameField;
    private JTextField teamField;
    private JTextField championshipsField;
    private JTextField pointsField;
    private JTextField podiumsField;
    private JTextField winsField;
    private ArrayList<Echipa> echipe;

    public DriverEditor(Frame parent, Driver driver, ArrayList<Echipa> echipe) {
        super(parent, "Editare Driver", true);
        this.driver = driver;
        this.echipe = echipe;

        nameField = new JTextField(driver.getNume());
        teamField = new JTextField(driver.getNumeEchipa());
        championshipsField = new JTextField(String.valueOf(driver.getCampionate()));
        pointsField = new JTextField(String.valueOf(driver.getNrPuncte()));
        podiumsField = new JTextField(String.valueOf(driver.getNrPodium()));
        winsField = new JTextField(String.valueOf(driver.getNrCastig()));

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.add(new JLabel("Nume:"));
        panel.add(nameField);
        panel.add(new JLabel("Echipă:"));
        panel.add(teamField);
        panel.add(new JLabel("Campionate:"));
        panel.add(championshipsField);
        panel.add(new JLabel("Puncte:"));
        panel.add(pointsField);
        panel.add(new JLabel("Podiumuri:"));
        panel.add(podiumsField);
        panel.add(new JLabel("Victori:"));
        panel.add(winsField);

        JButton saveButton = new JButton("Salvează");
        saveButton.addActionListener(e -> {
            driver.setNume(nameField.getText());
            driver.setNumeEchipa(teamField.getText());
            driver.setCampionate(Integer.parseInt(championshipsField.getText()));
            driver.setNrPuncte(Integer.parseInt(pointsField.getText()));
            driver.setNrPodium(Integer.parseInt(podiumsField.getText()));
            driver.setNrCastig(Integer.parseInt(winsField.getText()));
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }
}