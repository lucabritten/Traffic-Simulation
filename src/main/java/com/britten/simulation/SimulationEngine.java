package com.britten.simulation;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.domain.Vehicle;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationEngine {

    private List<Vehicle> vehicles;
    private List<Intersection> intersections;
    private MovementEngine movementEngine;
    private List<Road> allRoads;
    private CollisionResolver collisionResolver;

    private final List<SimulationEventListener> listeners;
    private int globalTick = 0;

    /**
     * vehicles and intersections may be immutable;
     * SimulationEngine will internally copy them into mutable lists.
     */
    public SimulationEngine(List<Vehicle> vehicles, List<Intersection> intersections, MovementEngine movementEngine){
        this.vehicles = new ArrayList<>(vehicles);
        this.intersections = new ArrayList<>(intersections);
        this.movementEngine = movementEngine;
        collisionResolver = new CollisionResolver();

        allRoads = new ArrayList<>();
        intersections.forEach(intersection -> {
            allRoads.addAll(intersection.getOutgoingRoads());
            // attach publisher to each per-road entry light if present
            var entryLights = intersection.getEntryLights();
            if (entryLights != null) {
                entryLights.values().forEach(l -> { if (l != null) l.setPublisher(this::publish); });
            }
        });

        movementEngine.setPublisher(this::publish);

        listeners = new ArrayList<>();
    }

    private void tick(int delta, int tick) {
        allRoads.forEach(Road::sortVehicles);
        // update intersection phase controllers and apply current phases
        intersections.forEach(i -> {
            if (i.getController() != null) {
                i.getController().update(i, tick);
                i.getController().applyPhase(i);
            }
        });
        Snapshot snapshot = createSnapshot(tick);
        vehicles.forEach(v ->  movementEngine.computeNext(v, snapshot, delta));

       // vehicles.forEach(v -> System.out.println("before collisionResolver call: np: " + v.getNextPosition() + ", ap: " + v.getPosition()));
        collisionResolver.resolve(snapshot, vehicles);

        applyNextStates(tick);

        allRoads.forEach(Road::sortVehicles);
        vehicles.removeIf(Vehicle::hasDestinationReached);


        vehicles.forEach(v -> {
            var road = v.getCurrentRoad();
            var intersection = road.getTo();
            var light = intersection.getLightFor(road);
            var state = (light == null) ? "NONE" : light.getState().name();

            System.out.printf(
                    "Vehicle %d | Speed: %d | Road %d -> %d | Pos %d | TL[%d <- %s] = %s%n",
                    v.getId(),
                    v.getSpeed(),
                    road.getFrom().getId(),
                    road.getTo().getId(),
                    v.getPosition(),
                    intersection.getId(),
                    road,
                    state
            );
        });
        globalTick++;
    }

    public void runForTicks(int totalTicks){
        for (int j = 0; j < totalTicks; j++) {
            tick(1, globalTick);
            System.out.println("Tick " + globalTick);
        }
    }

    public void runUntilDone(){
        boolean isFinished = false;
        while (!isFinished){
            tick(1, globalTick);
            isFinished = vehicles.stream()
                    .filter(v -> v.isRoutingEnabled() && !v.hasDestinationReached())
                    .toList()
                    .isEmpty();
        }
    }

    public void applyNextStates(int tick) {
        for(Vehicle vehicle : vehicles){
            Road oldRoad = vehicle.getCurrentRoad();
            Road newRoad = vehicle.getNextRoad();
            int newPos = vehicle.getNextPosition();
            int newSpeed = vehicle.getNextSpeed();

            if(newRoad == null) {
                newRoad = oldRoad;
            }

            if(newRoad != oldRoad){

                publish(new SimulationEvent(
                        "VEHICLE_ENTERED_ROAD",
                        tick,
                        Map.of(
                                "vehicleId", vehicle.getId(),
                                "fromRoad", oldRoad,
                                "toRoad", newRoad
                        )
                ));
                oldRoad.removeVehicle(vehicle);
                vehicle.setCurrentRoad(newRoad);
            }

            vehicle.setPosition(newPos);
            vehicle.setSpeed(newSpeed);

            if(!newRoad.getVehiclesOnRoad().contains(vehicle))
                newRoad.addVehicle(vehicle);
        }

        for(Intersection intersection : intersections){
            for(Road road : intersection.getOutgoingRoads()) {
                road.sortVehicles();
            }
        }
    }

    private Snapshot createSnapshot(int tick){
        Map<Road, List<Vehicle>> roadCopy = new HashMap<>();
        Map<Road,String> trafficCopy = new HashMap<>();
        Map<Road, TrafficLight> trafficLightMap = new HashMap<>();

        for(Intersection intersection : intersections){
            // snapshot per incoming road -> traffic light mapping
            for (Road road : intersection.getIncomingRoads()){
                roadCopy.put(road, new ArrayList<>(road.getVehiclesOnRoad()));
                TrafficLight light = intersection.getLightFor(road);
                trafficCopy.put(road, (light == null) ? "RED" : light.getState().name());
                trafficLightMap.put(road, light);
            }
        }
        return new Snapshot(roadCopy, trafficCopy, tick);
    }

    public void addListener(SimulationEventListener listener){
        listeners.add(listener);
    }

    public void publish(SimulationEvent event){
        listeners.forEach(l -> l.onEvent(event));
    }

    public int getGlobalTick(){
        return globalTick;
    }
}
