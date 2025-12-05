package com.britten.domain;

public abstract class Vehicle {

    private final int id;
    private Road currentRoad;
    private int position;
    protected double speed;

    public Vehicle(int id, Road currentRoad, double speed){
        if(currentRoad == null)
            throw new IllegalArgumentException("Current road cannot be null!");
        this.id = id;
        this.currentRoad = currentRoad;
        this.speed = speed;
        this.position = 0;
    }

    public int getId() {
        return id;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public int getPosition() {
        return position;
    }

    public double getSpeed(){
        return speed;
    }

    public void setPosition(int position){
        if(position < this.position)
            throw new IllegalArgumentException("A vehicle is not allowed to drive backwards!");
        if(position > currentRoad.getLength())
            throw new IllegalArgumentException("Value position is greater than length of current road.");

        this.position = position;
    }

    public void setCurrentRoad(Road currentRoad){
        if(currentRoad == null)
            throw new IllegalArgumentException("Road cannot be null.");
        this.currentRoad = currentRoad;
    }

    public void setSpeed(double speed){
        if(speed < 0)
            throw new IllegalArgumentException("Speed cannot be negative.");

        this.speed = speed;
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
