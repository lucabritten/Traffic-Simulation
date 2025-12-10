package com.britten.factory;

import com.britten.control.Phase;
import com.britten.control.PhaseController;
import com.britten.domain.*;
import com.britten.routing.DijkstraRoutePlanner;
import com.britten.routing.RoadNetwork;
import com.britten.routing.RoutePlanner;

import java.util.List;
import java.util.Set;

public class SquareGraphFactory implements GraphFactory{

    private List<Intersection> intersections;
    private List<Road> roads;

    @Override
    public RoadNetwork buildNetwork() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);
        Intersection i3 = new Intersection(3);
        Intersection i4 = new Intersection(4);

        Road r1 = new Road(i1, i2, 50);
        Road r2 = new Road(i2, i3, 50);
        Road r3 = new Road(i3, i4, 50);
        Road r4 = new Road(i4, i1, 50);

        TrafficLight t1 = new TrafficLight(1);
        TrafficLight t2 = new TrafficLight(2);
        TrafficLight t3 = new TrafficLight(3);
        TrafficLight t4 = new TrafficLight(4);

        // outgoing
        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);
        i3.addOutgoingRoad(r3);
        i4.addOutgoingRoad(r4);

        // incoming & per-road traffic lights
        i2.addIncomingRoad(r1, t1);
        i3.addIncomingRoad(r2, t2);
        i4.addIncomingRoad(r3, t3);
        i1.addIncomingRoad(r4, t4);

        // ========== NEW: PhaseController Setup ==========
        Phase nsPhase = new Phase(
                Set.of(r1, r3),   // North–South roads
                5                 // duration
        );
        Phase ewPhase = new Phase(
                Set.of(r2, r4),   // East–West roads
                5
        );

        PhaseController controller1 = new PhaseController(List.of(nsPhase, ewPhase));
        PhaseController controller2 = new PhaseController(List.of(nsPhase, ewPhase));
        PhaseController controller3 = new PhaseController(List.of(nsPhase, ewPhase));
        PhaseController controller4 = new PhaseController(List.of(nsPhase, ewPhase));

        i1.setController(controller1);
        i2.setController(controller2);
        i3.setController(controller3);
        i4.setController(controller4);

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
