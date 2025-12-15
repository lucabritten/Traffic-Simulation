package com.britten.domain;

import com.britten.control.PhaseController;

import java.util.*;

public class Intersection {

    private int id;
    private Set<Road> outgoingRoads;
    private Set<Road> incomingRoads;
    Map<Road, TrafficLight> entryLights;
    PhaseController controller;

    public Intersection(int id) {
        this.id = id;
        this.outgoingRoads = new LinkedHashSet<>();
        this.incomingRoads = new LinkedHashSet<>();
        entryLights = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Set<Road> getOutgoingRoads() {
        return outgoingRoads;
    }

    public TrafficLight getLightFor(Road road){
        return entryLights.get(road);
    }

    public void addOutgoingRoad(Road road){
        if(road == null)
            throw new IllegalArgumentException("Road cannot be null!");
        if(checkForDuplicateRoad(road))
            throw new IllegalArgumentException("Road already exists as outgoing road at this intersection!");
        if(road.getFrom() != this)
            throw new IllegalArgumentException("Road needs to be outgoing from this intersection!");

        outgoingRoads.add(road);
    }

    public void addOutgoingRoads(List<Road> roads){
        outgoingRoads.addAll(roads);
    }

    public void addIncomingRoad(Road road, TrafficLight light){
        incomingRoads.add(road);
        entryLights.put(road, light);
    }

    public Map<Road, TrafficLight> getEntryLights(){
        return entryLights;
    }

    public void setEntryLights(Map<Road,TrafficLight> map){
        entryLights = map;
    }

    public Set<Road> getIncomingRoads() {
        return incomingRoads;
    }

    public PhaseController getController() {
        return controller;
    }

    public void setController(PhaseController controller) {
        this.controller = controller;
    }

    private boolean checkForDuplicateRoad(Road road){
        Road existing = outgoingRoads.stream()
                .filter(r -> r.getTo() == road.getTo())
                .findFirst()
                .orElse(null);

        return existing != null;
    }

    public int getVehiclesWaitingAtStopline(Road road){
        if(road == null)
            throw new IllegalArgumentException("Road must not be null");

        if(!incomingRoads.contains(road))
            return 0;

        TrafficLight light = entryLights.get(road);

        return (int) road.getVehiclesOnRoad().stream()
                .filter(v -> v.getSpeed() == 0)
                .filter(v -> v.getPosition() >= road.getLength() - 2)
                .filter(v -> light == null
                        || light.getState().equals(TrafficLight.State.RED)
                        || light.getState().equals(TrafficLight.State.YELLOW)
                )
                .count();
    }

    public int getQueueLength(Road road) {
        if (road == null) {
            throw new IllegalArgumentException("Road must not be null");
        }

        if (!incomingRoads.contains(road)) {
            return 0;
        }

        return (int) road.getVehiclesOnRoad().stream()
                .filter(v -> v.getSpeed() == 0)
                .count();
    }


    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", outgoingRoads=" + outgoingRoads +
                ", trafficLight=" + entryLights +
                '}';
    }
}
