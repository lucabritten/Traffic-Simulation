package com.britten.factory;

import com.britten.domain.Car;
import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.simulation.MovementEngine;
import com.britten.simulation.SimulationEngine;

import java.util.List;

public class DemoGraphFactory {

    public static SimulationEngine buildSquareSimulation(){

        Intersection i1 = new Intersection(1, new TrafficLight(5, 2, 5));
        Intersection i2 = new Intersection(2, new TrafficLight(5,2,5));
        Intersection i3 = new Intersection(3, new TrafficLight(5,2,5));
        Intersection i4 = new Intersection(4, new TrafficLight(5,2,5));

        Road r1 = new Road(i1, i2, 7);
        Road r2 = new Road(i2, i3, 7);
        Road r3 = new Road(i3, i4, 7);
        Road r4 = new Road(i4, i1, 7);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);
        i3.addOutgoingRoad(r3);
        i4.addOutgoingRoad(r4);



        Car car = new Car(1, r1);

        return new SimulationEngine(
                List.of(car),
                List.of(i1,i2,i3,i4),
                new MovementEngine()
        );
    }
}
