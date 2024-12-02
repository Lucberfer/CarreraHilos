package com.mycompany.carrerahilos;

import java.util.*;

/**
 * @author lucas
 */

public class Race {
    private int raceDistance; // Total distance of the race
    private List<Car> cars; // List of participating cars
    private Map<Car, Long> raceTimes; // Map to store finish times for cars

    // Constructs a Race object with the specified race distance.
    public Race(int raceDistance) {
        if (raceDistance <= 0) {
            throw new IllegalArgumentException("Race distance must be greater than zero.");
        }
        this.raceDistance = raceDistance;
        this.cars = new ArrayList<>();
        this.raceTimes = new HashMap<>();
        initializeCars();
    }

    // Initializes the cars with random speeds and adds them to the race.
    private void initializeCars() {
        for (int i = 1; i <= 4; i++) { // Fixed number of cars: 4
            cars.add(new Car("Car " + i, (int) (Math.random() * 10 + 5))); // Speed between 5 and 15
        }
    }

    // Gets the list of participating cars.
    public List<Car> getCars() {
        return cars;
    }

    // Checks if the race is over (any car has reached or exceeded the distance).
    public boolean isRaceOver() {
        return cars.stream().anyMatch(car -> car.getDistanceCovered() >= raceDistance);
    }

    // Gets the first car to finish the race.
    public Car getWinner() {
        return cars.stream().filter(car -> car.getDistanceCovered() >= raceDistance).findFirst().orElse(null);
    }

    // Records the finish time of a car if not already recorded.
    public synchronized void recordTime(Car car, long time) {
        if (!raceTimes.containsKey(car)) {
            raceTimes.put(car, time);
        }
    }

    // Gets the map of cars and their respective finish times.
    public Map<Car, Long> getRaceTimes() {
        return raceTimes;
    }

    // Gets the total distance of the race.
    public int getRaceDistance() {
        return raceDistance;
    }

    // Resets the race by clearing times and resetting car distances.
    public void resetRace() {
        raceTimes.clear();
        for (Car car : cars) {
            car.reset();
        }
    }
}