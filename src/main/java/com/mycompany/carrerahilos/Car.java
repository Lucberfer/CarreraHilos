package com.mycompany.carrerahilos;

/**
 * @author lucas
 */

public class Car {
    private String name;
    private int distanceCovered;
    private int speed;

    public Car(String name, int speed) {
        this.name = name;
        this.speed = speed;
        this.distanceCovered = 0;
    }

    public String getName() {
        return name;
    }

    public int getDistanceCovered() {
        return distanceCovered;
    }

    public int getSpeed() {
        return speed;
    }

    public void advance() {
        distanceCovered += speed;
    }
}