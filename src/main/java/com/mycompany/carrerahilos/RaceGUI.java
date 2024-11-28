package com.mycompany.carrerahilos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * @author lucas
 */
public class RaceGUI extends JFrame {

    private JTextField distanceField;
    private List<JLabel> carLabels;
    private Race race;
    private final int TRACK_WIDTH = 800;

    public RaceGUI() {

        super("Car Race");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {

        setupTopPanel();
        setupRaceTrack();
    }

    private void setupTopPanel() {

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Race Distance:"));
        distanceField = new JTextField(5);
        topPanel.add(distanceField);

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> startRace());
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void setupRaceTrack() {

        JPanel racePanel = new JPanel();
        racePanel.setLayout(null);
        racePanel.setPreferredSize(new Dimension(TRACK_WIDTH, 300));
        add(racePanel, BorderLayout.CENTER);

        carLabels = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            JLabel carLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("car" + (i + 1) + ".png")));
            carLabel.setBounds(0, i * 70 + 20, 50, 50);
            carLabels.add(carLabel);
            racePanel.add(carLabel);
        }
    }

    private void startRace() {

        int raceDistance;
        try {
            raceDistance = Integer.parseInt(distanceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid race distance.");
            return;
        }

        race = new Race(raceDistance);
        new Thread(this::runRace).start();
    }

    private void runRace() {

        long startTime = System.currentTimeMillis();

        while (!race.isRaceOver()) {
            synchronized (this) {
                for (int i = 0; i < race.getCars().size(); i++) {
                    Car car = race.getCars().get(i);
                    car.advance();
                    int xPosition = Math.min(car.getDistanceCovered() * TRACK_WIDTH / race.getCars().size(), TRACK_WIDTH - 50);
                    carLabels.get(i).setLocation(xPosition, carLabels.get(i).getY());

                    if (car.getDistanceCovered() >= race.getCars().size() * 100) {
                        race.recordTime(car, System.currentTimeMillis() - startTime);
                    }
                }
            }

            sleep(200);
        }

        showResults();
    }

    private void showResults() {

        Map<Car, Long> raceTimes = race.getRaceTimes();

        StringBuilder results = new StringBuilder("Race Results:\n");
        raceTimes.forEach((car, time) -> results.append(car.getName())
                .append(" finished in ")
                .append(time / 1000.0)
                .append(" seconds\n"));

        JOptionPane.showMessageDialog(this, results.toString());
    }

    private void sleep(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
