package com.britten.domain;

import java.util.List;

public abstract class Vehicle {

    private final int id;

    private List<Road> route;
    private int routeIndex;
    private boolean routingEnabled;

    private Road currentRoad;
    private int position;
    private int speed;
    private final int defaultSpeed;

    private Road nextRoad;
    private int nextPosition;
    private int nextSpeed;

    public Vehicle(int id, Road currentRoad, int speed, int defaultSpeed){
        if(currentRoad == null)
            throw new IllegalArgumentException("Current road cannot be null!");

        if(speed <= 0 || defaultSpeed <= 0)
            throw new IllegalArgumentException("Speed and default speed cannot be negative or zero!");
        this.id = id;
        this.currentRoad = currentRoad;
        this.speed = speed;
        this.defaultSpeed = defaultSpeed;
        this.position = 0;
        this.routingEnabled = false;

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
    public boolean isRoutingEnabled(){
        return routingEnabled;
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
        if(nextPosition < 0 )
            throw new IllegalArgumentException("Next position cannot be negative or greater than next road length.");
        this.nextPosition = nextPosition;
    }

    public int getNextSpeed() {
        return nextSpeed;
    }

    public void setNextSpeed(int nextSpeed) {
        if(nextSpeed < 0)
            throw new IllegalArgumentException("Next speed cannot be negative.");
        this.nextSpeed = nextSpeed;
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
        if (route == null)
            return null;
        return (routeIndex < route.size()) ? route.get(routeIndex) : null;
    }

    public void advanceToNextRoad(){
        routeIndex++;
    }

    public boolean hasDestinationReached(){
        if (route == null && !routingEnabled)
            return false;
        if(route == null)
            return true;

        return routeIndex >= route.size();
    }

    public void restoreSpeed(){
        this.speed = defaultSpeed;
    }

    public void resetNextSpeedToDefault() {
        this.nextSpeed = defaultSpeed;
    }

    public void enableRouting(){
        if (route == null || route.isEmpty())
            throw new IllegalStateException("Cannot enable routing without a route set.");
        routingEnabled = true;
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
