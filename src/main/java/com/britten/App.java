package com.britten;

import com.britten.domain.Intersection;
import com.britten.domain.Vehicle;
import com.britten.factory.SquareGraphFactory;
import com.britten.factory.GraphFactory;
import com.britten.logging.ConsoleLogger;
import com.britten.routing.RoadNetwork;
import com.britten.simulation.MovementEngine;
import com.britten.simulation.SimulationEngine;

import java.util.List;

public class App {
    public static void main(String[] args) {

        GraphFactory factory = new SquareGraphFactory();

        RoadNetwork network = factory.buildNetwork();

        List<Intersection> intersections = factory.getIntersections();

        List<Vehicle> vehicles = factory.createVehicles(network);

        SimulationEngine engine = new SimulationEngine(
                vehicles,
                intersections,
                new MovementEngine()
        );

        engine.addListener(new ConsoleLogger());
        engine.runForTicks(100);
    }
}
