package com.britten.domain;

import java.util.*;

public class Road {

    private Intersection from;
    private Intersection to;
    private int length;
    private List<Vehicle> vehiclesOnRoad;

    public Road(Intersection from, Intersection to, int length) {
        if(length <= 0)
            throw new IllegalArgumentException("length cannot be negative or zero");
        if(from == null || to == null)
            throw new IllegalArgumentException("Intersection from and to are not allowed to be null!");

        this.from = from;
        this.to = to;
        this.length = length;
        vehiclesOnRoad = new ArrayList<>();
    }

    public List<Vehicle> getVehiclesOnRoad(){
        return vehiclesOnRoad;
    }


    public void addVehicle(Vehicle vehicle){
        if(!vehiclesOnRoad.contains(vehicle)) {
            vehiclesOnRoad.add(vehicle);
            sortVehicles();
        }
    }

    public void removeFirstVehicle(){
        if(!vehiclesOnRoad.isEmpty())
            vehiclesOnRoad.remove(0);
    }

    public void removeVehicle(Vehicle vehicle){
        vehiclesOnRoad.remove(vehicle);
    }

    public int getLength() {
        return length;
    }

    public Intersection getTo() {
        return to;
    }

    public Intersection getFrom() {
        return from;
    }

    public Vehicle getVehicleAheadOf(Vehicle vehicle){
        sortVehicles();
        int index = vehiclesOnRoad.indexOf(vehicle);
        if(index <= 0) return null;
        return vehiclesOnRoad.get(index - 1);
    }

    public boolean isFirst(Vehicle vehicle){
        return getVehicleAheadOf(vehicle) == null;
    }

    public void sortVehicles(){
        vehiclesOnRoad.sort(Comparator.comparingInt(Vehicle::getPosition).reversed());
    }

    @Override
    public String toString() {
        return "Road{" +
                "from=" + from.getId() +
                ", to=" + to.getId() +
                ", length=" + length +
                '}';
    }
}
