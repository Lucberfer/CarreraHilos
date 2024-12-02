package com.mycompany.carrerahilos;

/**
 * @author lucas
 */

public class Car {
    private String name; // Name of the car
    private int distanceCovered; // Distance covered by the car
    private int speed; // Speed of the car

    // Constructs a Car object with the given name and speed.
    public Car(String name, int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("La velocidad no puede ser negativa.");
        }
        this.name = name;
        this.speed = speed;
        this.distanceCovered = 0;
    }

    // Gets the name of the car.
    public String getName() {
        return name;
    }

    // Gets the distance covered by the car.
    public int getDistanceCovered() {
        return distanceCovered;
    }

    // Gets the speed of the car.
    public int getSpeed() {
        return speed;
    }

    // Advances the car based on its speed.
    public void advance() {
        distanceCovered += speed;
    }

    // Resets the distance covered by the car to zero.
    public void reset() {
        distanceCovered = 0;
    }

    //Returns a string representation of the car.
    @Override
    public String toString() {
        return String.format("Car{name='%s', speed=%d, distanceCovered=%d}", name, speed, distanceCovered);
    }
}