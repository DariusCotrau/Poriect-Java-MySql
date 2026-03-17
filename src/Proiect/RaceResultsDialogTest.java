package Proiect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RaceResultsDialogTest {

    private RaceResultsDialog raceResultsDialog;
    private ArrayList<Driver> drivers;

    @BeforeEach
    public void setUp() {
        drivers = new ArrayList<>();
        drivers.add(new Driver(1,"Lewis Hamilton", "Mercedes", 0,0,0,0));
        drivers.add(new Driver(2,"Max Verstappen", "Red Bull", 0,0,0,0));
        drivers.add(new Driver(3,"Charles Leclerc", "Ferrari", 0,0,0,0));
        drivers.add(new Driver(4,"Sergio Perez", "Red Bull", 0,0,0,0));
        drivers.add(new Driver(5,"Carlos Sainz", "Ferrari", 0,0,0,0));
        drivers.add(new Driver(6,"George Russell", "Mercedes", 0,0,0,0));
        drivers.add(new Driver(7,"Lando Norris", "McLaren", 0,0,0,0));
        drivers.add(new Driver(8,"Oscar Piastri", "McLaren", 0,0,0,0));
        drivers.add(new Driver(9,"Pierre Gasly", "Alpine", 0,0,0,0));
        drivers.add(new Driver(10,"Esteban Ocon", "Alpine", 0,0,0,0));

        GP testGP = new GP("Circuitul de la Abu Dhabi", 55, LocalDate.of(2024, 11, 26), TimeOfDay.NIGHT, false);
        raceResultsDialog = new RaceResultsDialog(null, testGP, drivers);

        simulateDriverSelection();
    }

    private void simulateDriverSelection() {
        JComboBox<String>[] positionComboBoxes = raceResultsDialog.getPositionComboBoxes();

        for (int i = 0; i < positionComboBoxes.length; i++) {
            positionComboBoxes[i].setSelectedItem(drivers.get(i).getNume());
        }
    }

    @Test
    public void testAssignPoints_CorrectAssignment() {
        raceResultsDialog.assignPoints();

        assertEquals(25, drivers.get(0).getNrPuncte(), "Poziția 1 ar trebui să primească 25 puncte.");
        assertEquals(18, drivers.get(1).getNrPuncte(), "Poziția 2 ar trebui să primească 18 puncte.");
        assertEquals(15, drivers.get(2).getNrPuncte(), "Poziția 3 ar trebui să primească 15 puncte.");
        assertEquals(12, drivers.get(3).getNrPuncte(), "Poziția 4 ar trebui să primească 12 puncte.");
        assertEquals(10, drivers.get(4).getNrPuncte(), "Poziția 5 ar trebui să primească 10 puncte.");
        assertEquals(8, drivers.get(5).getNrPuncte(), "Poziția 6 ar trebui să primească 8 puncte.");
        assertEquals(6, drivers.get(6).getNrPuncte(), "Poziția 7 ar trebui să primească 6 puncte.");
        assertEquals(4, drivers.get(7).getNrPuncte(), "Poziția 8 ar trebui să primească 4 puncte.");
        assertEquals(2, drivers.get(8).getNrPuncte(), "Poziția 9 ar trebui să primească 2 puncte.");
        assertEquals(1, drivers.get(9).getNrPuncte(), "Poziția 10 ar trebui să primească 1 punct.");
    }

    @Test
    public void testAssignPoints_NoDuplicatePoints() {
        JComboBox<String>[] positionComboBoxes = raceResultsDialog.getPositionComboBoxes();
        positionComboBoxes[1].setSelectedItem(drivers.get(0).getNume());

        raceResultsDialog.assignPoints();

        assertEquals(25, drivers.get(0).getNrPuncte(), "Driverul duplicat ar trebui să primească doar 25 puncte.");
        assertEquals(0, drivers.get(1).getNrPuncte(), "Driverul duplicat nu ar trebui să primească punctele Poziției 2.");
    }

    @Test
    public void testAssignPoints_EmptySelection() {
        JComboBox<String>[] positionComboBoxes = raceResultsDialog.getPositionComboBoxes();

        for (JComboBox<String> comboBox : positionComboBoxes) {
            comboBox.setSelectedItem(null);
        }

        raceResultsDialog.assignPoints();

        for (Driver driver : drivers) {
            assertEquals(0, driver.getNrPuncte(), "Niciun driver nu ar trebui să primească puncte.");
        }
    }
}
