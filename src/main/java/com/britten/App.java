package com.britten;

import com.britten.factory.DemoGraphFactory;
import com.britten.simulation.SimulationEngine;


public class App {
    public static void main(String[] args) {

        SimulationEngine engine = DemoGraphFactory.buildSquareSimulation();
        engine.runForTicks(100);
    }
}
