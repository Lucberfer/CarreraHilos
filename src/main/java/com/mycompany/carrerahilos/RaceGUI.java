package com.mycompany.carrerahilos;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * @author lucas
 */

public class RaceGUI extends JFrame {
    private JTextField distanceField; // Input field for race distance
    private List<JProgressBar> progressBars; // Progress bars for cars
    private List<JLabel> carLabels; // Labels for car images
    private Race race; // Race logic object
    private final int MAX_DISTANCE = 1000; // Maximum distance for progress bars

    // Constructor
    public RaceGUI() {
        super("Michis Rápidos y furiosos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // Adjusted window size
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
        topPanel.add(new JLabel("Distancia:"));
        distanceField = new JTextField(5);
        topPanel.add(distanceField);

        JButton startButton = new JButton("Empezar");
        startButton.addActionListener(e -> startRace());
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);
    }

    // Setup the race track panel
    private void setupRaceTrack() {
        JPanel racePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL resource = getClass().getClassLoader().getResource("BGround2.png");
                if (resource != null) {
                    ImageIcon background = new ImageIcon(resource);
                    g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        racePanel.setLayout(null); // Allow absolute positioning
        racePanel.setPreferredSize(new Dimension(1100, 400)); // Adjusted panel size
        add(racePanel, BorderLayout.CENTER);


        progressBars = new ArrayList<>();
        carLabels = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            // Create and add progress bars
            JProgressBar progressBar = new JProgressBar(0, MAX_DISTANCE);
            progressBar.setStringPainted(true);
            progressBar.setBounds(100, i * 100 + 70, 800, 30);
            progressBars.add(progressBar);
            racePanel.add(progressBar);

            // Create and add car images
            java.net.URL carResource = getClass().getClassLoader().getResource("car" + (i + 1) + ".png");
            if (carResource == null) {
                throw new IllegalArgumentException("Imagen no encontrada: car" + (i + 1) + ".png");
            }
            ImageIcon carIcon = new ImageIcon(new ImageIcon(carResource).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            JLabel carLabel = new JLabel(carIcon);
            carLabel.setBounds(20, i * 100 + 50, 80, 80); // Position above the progress bar
            carLabels.add(carLabel);
            racePanel.add(carLabel);
        }
    }


    // Start the race
    private void startRace() {
        int raceDistance;
        try {
            raceDistance = Integer.parseInt(distanceField.getText());
            if (raceDistance > MAX_DISTANCE || raceDistance <= 0) {
                JOptionPane.showMessageDialog(this, "Introduzca una distancia válida (1-" + MAX_DISTANCE + ")");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor introduzca una distancia válida.");
            return;
        }

        // Create a new race instance
        race = new Race(raceDistance);

        // Reset progress bars and car positions
        for (int i = 0; i < progressBars.size(); i++) {
            progressBars.get(i).setValue(0);
            carLabels.get(i).setLocation(20, i * 100 + 10); // Reset car positions
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

                    // Update progress bar
                    int progress = Math.min(car.getDistanceCovered() * MAX_DISTANCE / race.getRaceDistance(), MAX_DISTANCE);
                    progressBars.get(i).setValue(progress);

                    // Update car image position
                    int xPosition = 100 + progressBars.get(i).getWidth() * progress / MAX_DISTANCE;
                    carLabels.get(i).setLocation(xPosition, carLabels.get(i).getY());

                    if (car.getDistanceCovered() >= race.getRaceDistance()) {
                        race.recordTime(car, System.currentTimeMillis() - startTime);
                    }
                }
            }

            sleep(150); // Slower updates for smoother animation
        }

        showResults();
    }

    // Show results (winner and distances)
    private void showResults() {
        // Renombrar los carros dinámicamente
        List<String> customNames = Arrays.asList("Shrek", "McMenú", "Tofu", "Oreo");
        for (int i = 0; i < race.getCars().size(); i++) {
            Car car = race.getCars().get(i);
            car.setName(customNames.get(i)); // Renombrar cada carro usando una lista predefinida
        }

        // Crear datos para la tabla
        String[] columnNames = {"Car", "Distancia (m)"};
        Object[][] data = new Object[race.getCars().size()][2];

        for (int i = 0; i < race.getCars().size(); i++) {
            Car car = race.getCars().get(i);

            data[i][0] = car.getName();
            data[i][1] = car.getDistanceCovered();
        }

        // Crear tabla
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        // Agregar la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        // Crear el mensaje para el ganador
        Car winner = race.getWinner();
        String winnerMessage = winner != null
                ? "Ganador: " + winner.getName() + " - Distancia: " + winner.getDistanceCovered() + "m"
                : "No hay ganador.";

        JLabel winnerLabel = new JLabel(winnerMessage);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        winnerLabel.setForeground(Color.RED);

        // Crear panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(winnerLabel, BorderLayout.SOUTH);

        // Mostrar el JDialog
        JOptionPane.showMessageDialog(this, panel, "Resultados de la Carrera", JOptionPane.INFORMATION_MESSAGE);
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