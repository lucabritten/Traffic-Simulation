package com.britten.factory;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import com.britten.routing.DijkstraRoutePlanner;
import com.britten.routing.RoadNetwork;
import com.britten.routing.RoutePlanner;

import java.util.List;

public class DemoGraphFactory implements GraphFactory{

    private List<Intersection> intersections;
    private List<Road> roads;

    @Override
    public RoadNetwork buildNetwork() {

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

        intersections = List.of(i1,i2,i3,i4);
        roads = List.of(r1,r2,r3,r4);

        return new RoadNetwork(intersections, roads);
    }

    @Override
    public List<Vehicle> createVehicles(RoadNetwork network) {

        RoutePlanner planner = new DijkstraRoutePlanner();

        Road r1 = roads.get(0);
        Road r2 = roads.get(1);
        Road r3 = roads.get(2);
        Road r4 = roads.get(3);

        Intersection i1 = intersections.get(0);
        Intersection i2 = intersections.get(1);
        Intersection i3 = intersections.get(2);
        Intersection i4 = intersections.get(3);

        Car car1 = new Car(1, r1,1);
        car1.setRoute(planner.computeRoute(network, i1, i2));
        car1.enableRouting();

        Car car2 = new Car(2, r2,10);
        car2.setRoute(planner.computeRoute(network, i2, i4));
        car2.enableRouting();

        Car car3 = new Car(3, r3,10);
        car3.setRoute(planner.computeRoute(network, i3, i1));
        car3.enableRouting();

        Car car4 = new Car(4, r4,5);
        car4.setRoute(planner.computeRoute(network, i4, i2));
        car4.enableRouting();

        return List.of(car1, car2, car3, car4);
    }

    @Override
    public List<Intersection> getIntersections() {
        return intersections;
    }
}
