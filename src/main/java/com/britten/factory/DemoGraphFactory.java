package com.britten.factory;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.Car;
import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.simulation.MovementEngine;
import com.britten.simulation.SimulationEngine;
import com.britten.ui.AsciiRenderer;

import java.util.List;

public class DemoGraphFactory {

    public static SimulationEngine buildSquareSimulation(){

        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(3,1,5)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(3,1,5)));
        Intersection i3 = new Intersection(3, new TrafficLight(new FixedCycleStrategy(3,1,5)));
        Intersection i4 = new Intersection(4, new TrafficLight(new FixedCycleStrategy(3,1,5)));

        Road r1 = new Road(i1, i2, 50);
        Road r2 = new Road(i2, i3, 50);
        Road r3 = new Road(i3, i4, 50);
        Road r4 = new Road(i4, i1, 50);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);
        i3.addOutgoingRoad(r3);
        i4.addOutgoingRoad(r4);

        Car car1 = new Car(1, r1,1);
        Car car2 = new Car(2, r2,10);
        Car car3 = new Car(3, r3,10);
        Car car4 = new Car(4, r4,5);

        AsciiRenderer.defineSquareLayout(i1,i2,i3,i4);

        return new SimulationEngine(
                List.of(car1, car2, car3, car4),
                List.of(i1,i2,i3,i4),
                new MovementEngine()
        );
    }
}
