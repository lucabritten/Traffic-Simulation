package com.britten.simulation;

import com.britten.control.Phase;
import com.britten.control.PhaseController;
import com.britten.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationFlowTest {

    /**
     * Basic sanity test:
     * A car with speed 10 moves 5 ticks → pos = 50
     */
    @Test
    public void carDrivesAtDefaultSpeedForMultipleTicks() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);

        i1.addOutgoingRoad(r1);

        // Phase: r1 always green, no yellow, no red
        Phase phase = new Phase(Set.of(r1), 1000, 1, 1);
        PhaseController controller = new PhaseController(List.of(phase));
        i2.setController(controller);

        i2.addIncomingRoad(r1, new TrafficLight(1));

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car),
                List.of(i1, i2),
                new MovementEngine()
        );

        simulationEngine.runForTicks(5);

        assertThat(car.getPosition()).isEqualTo(50);
        assertThat(car.getCurrentRoad()).isEqualTo(r1);
        assertThat(car.getSpeed()).isEqualTo(10);
    }

    /**
     * Car stops at end of road when traffic light is red
     */
    @Test
    public void carDrivesToIntersectionWithRedTrafficLightAndStops() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);

        i1.addOutgoingRoad(r1);

        // Phase keeps r1 always RED
        Phase phase = new Phase(Set.of(), 1, 1, 1000);
        PhaseController controller = new PhaseController(List.of(phase));
        i2.setController(controller);

        i2.addIncomingRoad(r1, new TrafficLight(1));

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine sim = new SimulationEngine(
                List.of(car),
                List.of(i1, i2),
                new MovementEngine()
        );

        sim.runForTicks(9);
        assertThat(car.getPosition()).isEqualTo(90);

        sim.runForTicks(1);
        assertThat(car.getPosition()).isEqualTo(100);
        assertThat(car.getSpeed()).isZero();
        assertThat(i2.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.RED);
    }

    /**
     * Car waits at red, then moves when green.
     */
    @Test
    public void carWaitsInFrontOfTrafficLightAndContinuesWhenGreen() {

        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);
        Road r2 = new Road(i2, i1, 100);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        // Phase 1: r1 green for 9 ticks, then 1 yellow, 2 red
        // Then cycle repeats → r2 becomes green when r1 is red
        Phase p1 = new Phase(Set.of(r1), 9, 1, 2);
        Phase p2 = new Phase(Set.of(r2), 9, 1, 2);
        PhaseController controller = new PhaseController(List.of(p1, p2));
        i2.setController(controller);
        i1.setController(controller);

        i2.addIncomingRoad(r1, new TrafficLight(1));
        i1.addIncomingRoad(r2, new TrafficLight(2));

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine sim = new SimulationEngine(
                List.of(car),
                List.of(i1, i2),
                new MovementEngine()
        );

        // Reach end on tick 10, but red → must stop
        sim.runForTicks(11);

        assertThat(car.getPosition()).isEqualTo(100);
        assertThat(car.getSpeed()).isZero();
        assertThat(i2.getLightFor(r1).getState()).isNotEqualTo(TrafficLight.State.GREEN);

        // One more tick -> phase moves to r2 green
        sim.runForTicks(1);

        assertThat(i1.getLightFor(r2).getState()).isEqualTo(TrafficLight.State.GREEN);
        assertThat(car.getCurrentRoad()).isEqualTo(r2);
        assertThat(car.getPosition()).isEqualTo(10);
        assertThat(car.getSpeed()).isEqualTo(10);
    }

    /**
     * Car overflow into next road:
     * pos=90 on r1, speed=15 → overflow 5 onto r2
     */
    @Test
    void carJoinsNextRoadAndAppliesOverflowCorrectly() {

        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);
        Road r2 = new Road(i2, i1, 100);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        // r1 always green
        Phase p1 = new Phase(Set.of(r1), 1000, 1, 1);
        // r2 always green
        Phase p2 = new Phase(Set.of(r2), 1000, 1, 1);

        PhaseController controller = new PhaseController(List.of(p1, p2));
        i2.setController(controller);
        i1.setController(controller);

        i2.addIncomingRoad(r1, new TrafficLight(1));
        i1.addIncomingRoad(r2, new TrafficLight(2));

        Vehicle car = new Car(1, r1, 15);

        SimulationEngine sim = new SimulationEngine(
                List.of(car),
                List.of(i1, i2),
                new MovementEngine()
        );

        sim.runForTicks(6);

        assertThat(car.getPosition()).isEqualTo(90);

        sim.runForTicks(1);

        assertThat(car.getCurrentRoad()).isEqualTo(r2);
        assertThat(car.getPosition()).isEqualTo(5);
        assertThat(car.getSpeed()).isEqualTo(15);
    }

    /**
     * Test: queueing → second car must stop behind first.
     */
    @Test
    void testCarQueueingAtTrafficLight() {

        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);

        i1.addOutgoingRoad(r1);

        // Always red → forces queueing
        Phase p = new Phase(Set.of(), 1, 1, 1000);
        PhaseController controller = new PhaseController(List.of(p));
        i2.setController(controller);

        i2.addIncomingRoad(r1, new TrafficLight(1));

        Vehicle car1 = new Car(1, r1, 10);
        car1.setPosition(10);
        Vehicle car2 = new Car(2, r1, 12);

        SimulationEngine sim = new SimulationEngine(
                List.of(car1, car2),
                List.of(i1, i2),
                new MovementEngine()
        );

        sim.runForTicks(10);

        assertThat(car1.getPosition()).isEqualTo(100);
        assertThat(car1.getSpeed()).isZero();

        assertThat(car2.getPosition()).isEqualTo(98);
        assertThat(car2.getSpeed()).isZero();
    }
}
