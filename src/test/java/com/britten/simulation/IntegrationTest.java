package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private MovementEngine movement;
    private SimulationEngine sim;
    private Road r1;
    private Road r2;
    private Intersection i1;
    private Intersection i2;
    private Vehicle car;

    @BeforeEach
    void setup() {
        i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(100,1,100)));
        i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(100,1,100)));

        r1 = new Road(i1, i2, 100);
        r2 = new Road(i2,i1, 100);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

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
        i2.getTrafficLight().setState(TrafficLight.State.GREEN);

        sim.runForTicks(1);

        System.out.println(car.getPosition());
        assertThat(car.getCurrentRoad()).isNotEqualTo(r1);
    }

    @Test
    void vehicleStopsOnRed() {
        i2.getTrafficLight().setState(TrafficLight.State.RED);
        car.setPosition(100);
        sim.runForTicks(1);

        assertThat(car.getPosition()).isEqualTo(100);
        assertThat(car.getSpeed()).isZero();
    }
}
