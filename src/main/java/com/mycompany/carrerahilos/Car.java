package com.mycompany.carrerahilos;

/**
 * @author lucas
 */
public class Car {

    private String name;
    private int distanceCovered;
    private int speed;

    //Constructor
    public Car(String name, int speed) {

        this.name = name;
        this.speed = speed;
        this.distanceCovered = 0;
    }

    // Guetters
    public int getDistanceCovered() {
        return distanceCovered;
    }

    public String getName() {
        return name;
    }

    // Method to advance the car
    public void advance() {

        distanceCovered += speed;
    }

    public void reset() {

        distanceCovered = 0;
    }
}
