package com.britten.simulation;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.Vehicle;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventListener;
import com.britten.ui.AsciiRenderer;

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
            intersection.getTrafficLight().setPublisher(this::publish);
        });

        listeners = new ArrayList<>();
    }

    private void tick(int delta, int tick) {
        vehicles.removeIf(Vehicle::hasDestinationReached);
        allRoads.forEach(Road::sortVehicles);
        intersections.forEach(i -> i.getTrafficLight().update(tick));
        Snapshot snapshot = createSnapshot();
        vehicles.forEach(v ->  movementEngine.computeNext(v, snapshot, delta));

       // vehicles.forEach(v -> System.out.println("before collisionResolver call: np: " + v.getNextPosition() + ", ap: " + v.getPosition()));
        collisionResolver.resolve(snapshot, vehicles);

        applyNextStates(tick);

        allRoads.forEach(Road::sortVehicles);

        vehicles.forEach( v -> {
            System.out.printf(
                    "Vehicle %d | Speed: %d | Road %d -> %d | Pos %d | TL = %s \n",
                    v.getId(),
                    v.getSpeed(),
                    v.getCurrentRoad().getFrom().getId(),
                    v.getCurrentRoad().getTo().getId(),
                    v.getPosition(),
                    v.getCurrentRoad().getTo().getTrafficLight().getState());
        });
    }

    public void runForTicks(int totalTicks){
        for(int i = 0; i < totalTicks; i++){
            tick(1, i);
            System.out.println("Tick " + i);
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

    private Snapshot createSnapshot(){
        Map<Road, List<Vehicle>> roadCopy = new HashMap<>();
        Map<Road,String> trafficCopy = new HashMap<>();

        for(Intersection intersection : intersections){
            for (Road road : intersection.getOutgoingRoads()){
                roadCopy.put(road, new ArrayList<>(road.getVehiclesOnRoad()));
                trafficCopy.put(road,road.getTo().getTrafficLight().getState().name());
            }
        }
        return new Snapshot(roadCopy, trafficCopy);
    }

    public void addListener(SimulationEventListener listener){
        listeners.add(listener);
    }

    public void publish(SimulationEvent event){
        listeners.forEach(l -> l.onEvent(event));
    }
}
