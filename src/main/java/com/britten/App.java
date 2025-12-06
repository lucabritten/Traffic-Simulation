package com.britten;

import com.britten.domain.Intersection;
import com.britten.domain.Vehicle;
import com.britten.factory.DemoGraphFactory;
import com.britten.factory.GraphFactory;
import com.britten.routing.RoadNetwork;
import com.britten.simulation.MovementEngine;
import com.britten.simulation.SimulationEngine;

import java.util.List;

public class App {
    public static void main(String[] args) {

        GraphFactory factory = new DemoGraphFactory();

        RoadNetwork network = factory.buildNetwork();

        List<Intersection> intersections = factory.getIntersections();

        List<Vehicle> vehicles = factory.createVehicles(network);

        SimulationEngine engine = new SimulationEngine(
                vehicles,
                intersections,
                new MovementEngine()
        );

        engine.runForTicks(100);
    }
}
