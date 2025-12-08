package com.britten.domain;

import com.britten.control.PhaseController;

import java.util.*;

public class Intersection {

    private int id;
    private Set<Road> outgoingRoads;
    private Set<Road> incomingRoads;
    Map<Road, TrafficLight> entryLights;
    PhaseController controller;
    private TrafficLight trafficLight;

    public Intersection(int id, TrafficLight trafficLight) {
        if(trafficLight == null)
            throw new IllegalArgumentException("TrafficLight cannot be null!");

        this.id = id;
        this.outgoingRoads = new LinkedHashSet<>();
        this.incomingRoads = new LinkedHashSet<>();
        entryLights = new HashMap<>();
        this.trafficLight = trafficLight;
    }

    public int getId() {
        return id;
    }

    public Set<Road> getOutgoingRoads() {
        return outgoingRoads;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
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

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", outgoingRoads=" + outgoingRoads +
                ", trafficLight=" + trafficLight +
                '}';
    }
}
