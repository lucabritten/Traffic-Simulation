package com.britten.simulation;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Snapshot {

    private Map<Road, List<Vehicle>> roadVehicles;
    private Map<Road, String> trafficLightStates;
    private int tick = -1;

    public Snapshot(Map<Road, List<Vehicle>> roadVehicles, Map<Road, String> trafficLightStates){
        this.roadVehicles = roadVehicles;
        this.trafficLightStates = trafficLightStates;
    }

    public Snapshot(Map<Road, List<Vehicle>> roadVehicles, Map<Road, String> trafficLightStates, int tick){
        this(roadVehicles, trafficLightStates);
        this.tick = tick;
    }

    public Map<Road, List<Vehicle>> getRoadVehicles(){
        return roadVehicles;
    }

    public Map<Road, String> getLightStates() {
        return trafficLightStates;
    }

    public int getTick(){
        return tick;
    }

    public List<Vehicle> getVehiclesOn(Road road) {
        return roadVehicles.getOrDefault(road, List.of());
    }

    public Vehicle getFrontVehicle(Road road) {
        List<Vehicle> list = getVehiclesOn(road);
        return list.isEmpty() ? null : list.getFirst();
    }

    public Vehicle getSecondVehicle(Road road) {
        List<Vehicle> list = getVehiclesOn(road);
        return list.size() < 2 ? null : list.get(1);
    }

}
