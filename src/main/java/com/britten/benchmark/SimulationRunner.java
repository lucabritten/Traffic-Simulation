package com.britten.benchmark;

import com.britten.domain.Intersection;
import com.britten.domain.Vehicle;
import com.britten.factory.GraphFactory;
import com.britten.routing.RoadNetwork;
import com.britten.simulation.MovementEngine;
import com.britten.simulation.SimulationEngine;

import java.util.List;

public class SimulationRunner {

    public SimulationResult run(GraphFactory config){
        RoadNetwork network = config.buildNetwork();

        List<Vehicle> vehicles = config.createVehicles(network);

        List<Intersection> intersections = config.getIntersections();

        SimulationEngine engine = new SimulationEngine(
                vehicles,
                intersections,
                new MovementEngine()
        );

        MetricsCollector metricsCollector = new MetricsCollector();
        engine.addListener(metricsCollector);

        int ticks = engine.runUntilDone();
        return  metricsCollector.buildResult(ticks);
    }
}
