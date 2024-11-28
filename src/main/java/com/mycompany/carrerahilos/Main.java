package com.mycompany.carrerahilos;

import javax.swing.*;

/**
 * @author lucas
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RaceGUI raceGUI = new RaceGUI();
            raceGUI.setVisible(true);
        });
    }
}
