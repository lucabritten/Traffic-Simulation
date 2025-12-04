package com.britten.simulation;

import com.britten.domain.Intersection;
import com.britten.domain.Vehicle;

import java.util.List;

public class SimulationEngine {

    private List<Vehicle> vehicles;
    private List<Intersection> intersections;
    private MovementEngine movementEngine;

    public SimulationEngine(List<Vehicle> vehicles, List<Intersection> intersections, MovementEngine movementEngine){
        this.vehicles = vehicles;
        this.intersections = intersections;
        this.movementEngine = movementEngine;
    }

    public void tick(int delta) {
        intersections.forEach(i -> i.getTrafficLight().update());
        vehicles.forEach(v -> movementEngine.move(v, delta));

        vehicles.forEach( v -> {
            System.out.printf(
                    "Vehicle %d | Road %d -> %d | Pos %d | TL = %s \n", v.getId(),
                    v.getCurrentRoad().getFrom().getId(),
                    v.getCurrentRoad().getTo().getId(),
                    v.getPosition(),
                    v.getCurrentRoad().getTo().getTrafficLight().getState());
        });


    }

    public void runForTicks(int totalTicks){
        for(int i = 0; i < totalTicks; i++){
            tick(1);
            System.out.println("Tick " + i);
        }
    }
}
