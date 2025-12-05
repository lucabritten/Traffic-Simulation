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
        this.position = position;
    }

    public void setCurrentRoad(Road currentRoad){
        this.currentRoad = currentRoad;
    }

    public void setSpeed(double speed){
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
