package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.control.Phase;
import com.britten.control.PhaseController;
import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private MovementEngine movement;
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


        t1 = new TrafficLight(1,new FixedCycleStrategy(100,100,100));
        t2 = new TrafficLight(2,new FixedCycleStrategy(100,100,100));

        // outgoing
        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        // incoming & per-road traffic lights
        i1.addIncomingRoad(r2, t1);
        i2.addIncomingRoad(r1, t2);


        car = new Car(1,r1,10);

        movement = new MovementEngine();

        sim = new SimulationEngine(
                List.of(car),
                List.of(i1,i2),
                movement
                );
    }

    @Test
    void vehicleEntersNextRoadOnGreen() {
        car.setPosition(95);
        i2.getLightFor(car.getCurrentRoad()).setState(TrafficLight.State.GREEN);

        sim.runForTicks(1);

        System.out.println(car.getPosition());
        assertThat(car.getCurrentRoad()).isNotEqualTo(r1);
    }

    @Test
    void vehicleStopsOnRed() {
        car.setPosition(r1.getLength());
        i2.getLightFor(r1).setState(TrafficLight.State.RED);

        sim.runForTicks(1);

        assertThat(car.getPosition()).isEqualTo(100);
        assertThat(car.getSpeed()).isZero();
    }
}
