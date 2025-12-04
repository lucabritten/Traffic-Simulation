package com.britten.domain;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    private int id;
    private List<Road> outgoingRoads;
    private TrafficLight trafficLight;

    public Intersection(int id, TrafficLight trafficLight) {
        this.id = id;
        this.outgoingRoads = new ArrayList<>();
        this.trafficLight = trafficLight;
    }

    public int getId() {
        return id;
    }

    public List<Road> getOutgoingRoads() {
        return outgoingRoads;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void addRoad(Road road){
        outgoingRoads.add(road);
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
