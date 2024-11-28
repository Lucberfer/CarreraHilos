package com.mycompany.carrerahilos;

import java.util.*;

/**
 * @author lucas
 */

public class Race {
    private int raceDistance;
    private List<Car> cars;
    private Map<Car, Long> raceTimes;

    public Race(int raceDistance) {
        this.raceDistance = raceDistance;
        this.cars = new ArrayList<>();
        this.raceTimes = new HashMap<>();
        initializeCars();
    }

    private void initializeCars() {
        for (int i = 1; i <= 4; i++) {
            cars.add(new Car("Car " + i, (int) (Math.random() * 10 + 5))); // Speed between 5 and 15
        }
    }

    public List<Car> getCars() {
        return cars;
    }

    public boolean isRaceOver() {
        return cars.stream().anyMatch(car -> car.getDistanceCovered() >= raceDistance);
    }

    public Car getWinner() {
        return cars.stream().filter(car -> car.getDistanceCovered() >= raceDistance).findFirst().orElse(null);
    }

    public synchronized void recordTime(Car car, long time) {
        if (!raceTimes.containsKey(car)) {
            raceTimes.put(car, time);
        }
    }

    public Map<Car, Long> getRaceTimes() {
        return raceTimes;
    }

    public int getRaceDistance() {
        return raceDistance;
    }
}