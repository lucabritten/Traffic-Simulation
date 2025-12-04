package com.britten.domain;

public abstract class Vehicle {

    private final int id;
    private Road currentRoad;
    private double position;

    public Vehicle(int id, Road currentRoad){
        this.id = id;
        this.currentRoad = currentRoad;
    }

    public int getId() {
        return id;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public double getPosition() {
        return position;
    }

    public void move(double delta){
        this.position += delta;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", currentRoad=" + currentRoad +
                ", position=" + position +
                '}';
    }
}
