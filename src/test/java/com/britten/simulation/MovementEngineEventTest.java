package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.control.Phase;
import com.britten.control.PhaseController;
import com.britten.domain.*;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventPublisher;
import com.britten.testutil.TestEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MovementEngineEventTest {

    private MovementEngine movement;
    private TestEventPublisher publisher;
    private SimulationEngine sim;
    private Road r1;
    private Road r2;
    private Intersection i1;
    private Intersection i2;
    private Vehicle car;
    TrafficLight t1;
    TrafficLight t2;


    @BeforeEach
    void setup() {
        i1 = new Intersection(1);
        i2 = new Intersection(2);

        r1 = new Road(i1, i2, 100);
        r2 = new Road(i2, i1, 100);


        t1 = new TrafficLight(1);
        t2 = new TrafficLight(2);

        // outgoing
        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        // incoming & per-road traffic lights
        i1.addIncomingRoad(r2, t1);
        i2.addIncomingRoad(r1, t2);


        car = new Car(1, r1, 10);

        movement = new MovementEngine();

        publisher = new TestEventPublisher();
        sim = new SimulationEngine(
                List.of(car),
                List.of(i1, i2),
                movement
        );
        movement.setPublisher(publisher);
    }

    void setAlwaysGreen(Intersection intersection, Road road) {
        Phase phase = new Phase(Set.of(road), 1000, 1, 1);
        intersection.setController(
                new PhaseController(new FixedCycleStrategy(List.of(phase)))
        );
    }

    void setAlwaysRed(Intersection intersection) {
        Phase phase = new Phase(Set.of(), 1, 1, 1000);
        intersection.setController(
                new PhaseController(new FixedCycleStrategy(List.of(phase)))
        );
    }

    void setAlternatingPhases(Intersection intersection, Road r1, Road r2) {
        Phase p1 = new Phase(Set.of(r1), 9, 1, 1);
        Phase p2 = new Phase(Set.of(r2), 9, 1, 1);

        intersection.setController(
                new PhaseController(new FixedCycleStrategy(List.of(p1, p2)))
        );
    }

    @Test
    void vehicleStoppedEventIsEmittedOnce(){
        setAlwaysRed(i2);

        sim.runForTicks(15);
        System.out.println(publisher.events());

        assertThat(publisher.eventsOfType("VEHICLE_STOPPED"))
                .hasSize(1);
    }

    @Test
    void vehicleWaitingEventIsEmittedWhileStanding(){
        setAlwaysRed(i2);

        sim.runForTicks(13);

        assertThat(publisher.eventsOfType("VEHICLE_WAITING"))
                .hasSize(3);
    }

    @Test
    void vehicleStoppedThenWaitingOrder(){
        setAlwaysRed(i2);

        sim.runForTicks(12);

        assertThat(publisher.events())
                .extracting(SimulationEvent::type)
                .containsSubsequence(
                        "VEHICLE_STOPPED",
                        "VEHICLE_WAITING"
                );
    }

    @Test
    void vehicleArrivedIsEmittedExactlyOnce(){
        setAlwaysGreen(i2, r1);

        car.setRoute(List.of(r1,r2));

        sim.runForTicks(20);

        assertThat(publisher.eventsOfType("VEHICLE_ARRIVED"))
                .hasSize(1);
    }

    @Test
    void arrivalEventContainsCorrectData(){
        setAlwaysGreen(i2, r1);

        car.setRoute(List.of(r1,r2));

        sim.runForTicks(20);

        SimulationEvent event = publisher.eventsOfType("VEHICLE_ARRIVED").get(0);

        assertThat(event.data())
                .containsEntry("vehicleId", car.getId())
                .containsKey("intersectionId");
    }


}
