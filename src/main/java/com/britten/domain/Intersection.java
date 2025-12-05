package com.britten.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Intersection {

    private int id;
    private Set<Road> outgoingRoads;
    private TrafficLight trafficLight;

    public Intersection(int id, TrafficLight trafficLight) {
        if(trafficLight == null)
            throw new IllegalArgumentException("TrafficLight cannot be null!");

        this.id = id;
        this.outgoingRoads = new HashSet<>();
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

    public void addOutgoingRoad(Road road){
        if(road == null)
            throw new IllegalArgumentException("Road cannot be null!");
        if(checkForDuplicateRoad(road))
            throw new IllegalArgumentException("Road already exists as outgoing road at this intersection!");
        if(road.getFrom() != this)
            throw new IllegalArgumentException("Road needs to be outgoing from this intersection!");

        outgoingRoads.add(road);
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
