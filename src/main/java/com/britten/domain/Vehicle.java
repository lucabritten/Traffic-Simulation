package com.britten.domain;

public abstract class Vehicle {

    private final int id;

    private Road currentRoad;
    private int position;
    private int speed;

    private Road nextRoad;
    private int nextPosition;
    private int nextSpeed;
    private final int defaultSpeed;

    public Vehicle(int id, Road currentRoad, int speed, int defaultSpeed){
        if(currentRoad == null)
            throw new IllegalArgumentException("Current road cannot be null!");
        this.id = id;
        this.currentRoad = currentRoad;
        this.speed = speed;
        this.position = 0;
        currentRoad.addVehicle(this);
        this.defaultSpeed = defaultSpeed;
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

    public int getSpeed(){
        return speed;
    }

    public int getDefaultSpeed(){
        return defaultSpeed;
    }

    public void setPosition(int position){
        if(position > currentRoad.getLength())
            throw new IllegalArgumentException("Value position is greater than length of current road.");

        this.position = position;
    }

    public void setCurrentRoad(Road newRoad){
        if(newRoad == null)
            throw new IllegalArgumentException("Road cannot be null.");
        this.currentRoad = newRoad;
        this.currentRoad.addVehicle(this);
    }

    public void setSpeed(int speed){
        if(speed < 0)
            throw new IllegalArgumentException("Speed cannot be negative.");

        this.speed = speed;
    }

    public Road getNextRoad() {
        return nextRoad;
    }

    public void setNextRoad(Road nextRoad) {
        this.nextRoad = nextRoad;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public int getNextSpeed() {
        return nextSpeed;
    }

    public void setNextSpeed(int nextSpeed) {
        this.nextSpeed = nextSpeed;
    }

    public void restoreSpeed(){
        this.speed = defaultSpeed;
    }

    public void resetNextSpeedToDefault() {
        this.nextSpeed = defaultSpeed;
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
