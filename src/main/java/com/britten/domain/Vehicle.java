package com.britten.domain;

import java.util.List;

public abstract class Vehicle {

    private final int id;

    private List<Road> route;
    private int routeIndex;

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
        this.defaultSpeed = defaultSpeed;
        this.position = 0;

        currentRoad.addVehicle(this);
    }

    public Vehicle(int id, Road currentRoad, int speed, int defaultSpeed, List<Road> route){
        this(id,currentRoad,speed,defaultSpeed);
        this.route = route;
        this.routeIndex = 0;
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

    public void setRoute(List<Road> route){
        this.route = route;
        this.routeIndex = 0;
    }

    public Road peekNextRoad(){
        return (routeIndex < route.size()) ? route.get(routeIndex) : null;
    }

    public void advanceToNextRoad(){
        routeIndex++;
    }

    public boolean hasDestinationReached(){
        return routeIndex >= route.size();
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
