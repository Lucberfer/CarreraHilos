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
    private JTextField distanceField; // Input field for race distance
    private List<JProgressBar> progressBars; // Progress bars for cars
    private Race race; // Race logic object
    private final int MAX_DISTANCE = 1000; // Maximum distance for progress bars

    // Constructor
    public RaceGUI() {
        super("Car Race");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400); // Adjusted window size
        setLayout(new BorderLayout());
        initializeComponents();
    }

    // Initialize GUI components
    private void initializeComponents() {
        setupTopPanel();
        setupRaceTrack();
    }

    // Setup the top panel with input and start button
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

    // Setup the race track panel
    private void setupRaceTrack() {
        JPanel racePanel = new JPanel();
        racePanel.setLayout(new GridLayout(4, 1, 10, 10));
        racePanel.setPreferredSize(new Dimension(800, 300)); // Adjusted panel size
        add(racePanel, BorderLayout.CENTER);

        progressBars = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JProgressBar progressBar = new JProgressBar(0, MAX_DISTANCE);
            progressBar.setStringPainted(true);
            progressBars.add(progressBar);
            racePanel.add(progressBar);
        }
    }

    // Start the race
    private void startRace() {
        int raceDistance;
        try {
            raceDistance = Integer.parseInt(distanceField.getText());
            if (raceDistance > MAX_DISTANCE || raceDistance <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid distance (1-" + MAX_DISTANCE + ")");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid race distance.");
            return;
        }

        // Create a new race instance
        race = new Race(raceDistance);

        // Reset progress bars
        for (JProgressBar progressBar : progressBars) {
            progressBar.setValue(0);
        }

        // Start the race in a new thread
        new Thread(this::runRace).start();
    }

    // Run the race logic
    private void runRace() {
        long startTime = System.currentTimeMillis();

        while (!race.isRaceOver()) {
            synchronized (this) {
                for (int i = 0; i < race.getCars().size(); i++) {
                    Car car = race.getCars().get(i);
                    car.advance();
                    int progress = Math.min(car.getDistanceCovered() * MAX_DISTANCE / race.getRaceDistance(), MAX_DISTANCE);
                    progressBars.get(i).setValue(progress);

                    if (car.getDistanceCovered() >= race.getRaceDistance()) {
                        race.recordTime(car, System.currentTimeMillis() - startTime);
                    }
                }
            }

            sleep(500); // Slower updates for smoother animation
        }

        showResults();
    }

    // Show results (winner and distances)
    private void showResults() {
        Map<Car, Long> raceTimes = race.getRaceTimes();
        StringBuilder results = new StringBuilder("Race Results:\n");

        for (int i = 0; i < race.getCars().size(); i++) {
            Car car = race.getCars().get(i);
            results.append(car.getName())
                    .append(" - Distance: ").append(car.getDistanceCovered()).append("m")
                    .append(" - Time: ").append(raceTimes.getOrDefault(car, 0L) / 1000.0).append("s")
                    .append("\n");
        }

        Car winner = race.getWinner();
        if (winner != null) {
            results.append("\nWinner: ").append(winner.getName());
        }

        JOptionPane.showMessageDialog(this, results.toString());
    }

    // Pause for a given duration
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}