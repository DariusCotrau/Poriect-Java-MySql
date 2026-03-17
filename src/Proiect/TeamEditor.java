package Proiect;

import javax.swing.*;
import java.awt.*;

public class TeamEditor extends JDialog {
    private Echipa echipa;
    private JTextField nameField;
    private JTextField championshipsField;
    private JTextField pointsField;

    public TeamEditor(Frame parent, Echipa echipa) {
        super(parent, "Editare Echipa", true);
        this.echipa = echipa;

        nameField = new JTextField(echipa.getNume());
        championshipsField = new JTextField(String.valueOf(echipa.getNrCampionate()));
        pointsField = new JTextField(String.valueOf(echipa.getTotalPuncte()));

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Nume Echipa:"));
        panel.add(nameField);
        panel.add(new JLabel("Campionate:"));
        panel.add(championshipsField);
        panel.add(new JLabel("Puncte Totale:"));
        panel.add(pointsField);

        JButton saveButton = new JButton("Salvează");
        saveButton.addActionListener(e -> {
            echipa.setNume(nameField.getText());
            echipa.setNrCampionate(Integer.parseInt(championshipsField.getText()));
            echipa.setTotalPoints(Integer.parseInt(pointsField.getText()));
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
