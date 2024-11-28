package com.mycompany.carrerahilos;

import java.util.*;

/**
 * @author lucas
 */

public class Race {
    private int raceDistance;
    private List<Car> cars;
    private Map<Car, Long> raceTimes;

    public  Race(int raceDistance) {
        this.raceDistance = raceDistance;
        this.cars = new ArrayList<>();
        this.raceTimes = new HashMap<>();
        initializeCars();
    }

    public void initializeCars(){

        cars.add(new Car("Michi 1", (int) (Math.random() * 10 + 1)));
        cars.add(new Car("Michi 2", (int) (Math.random() * 10 + 1)));
        cars.add(new Car("Michi 3", (int) (Math.random() * 10 + 1)));
        cars.add(new Car("Michi 4", (int) (Math.random() * 10 + 1)));
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
        if(!raceTimes.containsKey(car)) {
            raceTimes.put(car, time);
        }
    }

    public Map<Car, Long> getRaceTimes() {

        return raceTimes;
    }

    public void resetRace() {

        cars.forEach(Car::reset);
        raceTimes.clear();
    }
}

