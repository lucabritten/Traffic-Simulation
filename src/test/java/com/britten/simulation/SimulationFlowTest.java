package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationFlowTest {


    /*
    Car drives 5 ticks normal
    speed 10
    start 0
    after 5 ticks -> pos50
    speed is default
     */
    @Test
    public void carDrivesAtDefaultSpeedForMultipleTicks(){
        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(2,2,2)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(2,2,2)));

        Road r1 = new Road(i1, i2, 100);

        i1.addOutgoingRoad(r1);

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car),
                List.of(i1,i2),
                new MovementEngine()
        );

        assertThat(car.getSpeed()).isEqualTo(10);
        assertThat(car.getSpeed()).isEqualTo(car.getDefaultSpeed());
        assertThat(car.getPosition()).isEqualTo(0);
        assertThat(car.getCurrentRoad()).isEqualTo(r1);

        simulationEngine.runForTicks(5);

        assertThat(car.getSpeed()).isEqualTo(10);
        assertThat(car.getSpeed()).isEqualTo(car.getDefaultSpeed());
        assertThat(car.getPosition()).isEqualTo(50);
        assertThat(car.getCurrentRoad()).isEqualTo(r1);
    }

    /*
    Car drives to TL and stops
    road 100
    speed 10
    start 0
    ampel red since tick6

    expect:
        after 9 ticks pos90,
         tick10 = 100 (stop),
         never pos > 100,
         Current speed 0
     */
    @Test
    public void carDrivesToIntersectionWithRedTrafficLightAndStops(){
        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(2,1,5)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(2,1,5)));

        int END_OF_ROAD = 100;
        Road r1 = new Road(i1, i2, END_OF_ROAD);

        i1.addOutgoingRoad(r1);

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car),
                List.of(i1,i2),
                new MovementEngine()
        );

        simulationEngine.runForTicks(9);

        assertThat(car.getSpeed()).isEqualTo(10);
        assertThat(car.getSpeed()).isEqualTo(car.getDefaultSpeed());
        assertThat(car.getPosition()).isEqualTo(90);

        simulationEngine.runForTicks(1);

        assertThat(car.getPosition()).isEqualTo(END_OF_ROAD);

        simulationEngine.runForTicks(1);

        assertThat(i2.getTrafficLight().getState()).isEqualTo(TrafficLight.State.RED);
        assertThat(car.getPosition()).isEqualTo(END_OF_ROAD);
        assertThat(car.getSpeed()).isZero();
    }

    /*
    Car waits in front of traffic light
    when green -> continues driving at default speed
    car is on new road
     */
    @Test
    public void carWaitsInFrontOfTrafficLightAndContinuesWhenGreen(){
        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(2,1,5)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(2,1,5)));

        int END_OF_ROAD = 100;
        Road r1 = new Road(i1, i2, END_OF_ROAD);
        Road r2 = new Road(i2,i1, END_OF_ROAD);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        Vehicle car = new Car(1, r1, 10);

        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car),
                List.of(i1,i2),
                new MovementEngine()
        );

        simulationEngine.runForTicks(12);

        assertThat(i1.getTrafficLight().getState()).isEqualTo(TrafficLight.State.RED);
        assertThat(car.getPosition()).isEqualTo(END_OF_ROAD);
        assertThat(car.getSpeed()).isZero();

        simulationEngine.runForTicks(1);

        assertThat(i1.getTrafficLight().getState()).isEqualTo(TrafficLight.State.GREEN);
        assertThat(car.getCurrentRoad()).isEqualTo(r2);
        assertThat(car.getPosition()).isEqualTo(10);
        assertThat(car.getSpeed()).isEqualTo(10);
    }

    /*
    Car speed 15
    Road A = 100
    Road b = 200
    A -> B
    car joins B as expected with 5 remaining distance 90(A) -> 5(B)
     */
    @Test
    void carJoinsNextRoadAndAppliesOverflowCorrectly(){
        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(100,1,1)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(100,1,1)));

        int END_OF_ROAD = 100;
        Road r1 = new Road(i1, i2, END_OF_ROAD);
        Road r2 = new Road(i2,i1, END_OF_ROAD);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        Vehicle car = new Car(1, r1, 15);

        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car),
                List.of(i1,i2),
                new MovementEngine()
        );

        simulationEngine.runForTicks(6);

        assertThat(i2.getTrafficLight().getState()).isEqualTo(TrafficLight.State.GREEN);
        assertThat(car.getPosition()).isEqualTo(90);

        simulationEngine.runForTicks(1);

        assertThat(car.getCurrentRoad()).isEqualTo(r2);
        assertThat(car.getSpeed()).isEqualTo(15);
        assertThat(car.getPosition()).isEqualTo(5);
    }

            /*
    Multiple car test
    cars don't overtake
     */
    @Test
    void testCarQueueingAtTrafficLight(){
        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(1,1,100)));
        Intersection i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(1,1,100)));

        int END_OF_ROAD = 100;
        Road r1 = new Road(i1, i2, END_OF_ROAD);
        Road r2 = new Road(i2,i1, END_OF_ROAD);

        i1.addOutgoingRoad(r1);
        i2.addOutgoingRoad(r2);

        Vehicle car1 = new Car(1, r1, 10);
        car1.setPosition(10);
        Vehicle car2 = new Car(2, r1, 12);


        SimulationEngine simulationEngine = new SimulationEngine(
                List.of(car1, car2),
                List.of(i1,i2),
                new MovementEngine()
        );

        simulationEngine.runForTicks(10);

        assertThat(car1.getPosition()).isEqualTo(100);
        assertThat(car1.getSpeed()).isEqualTo(0);

        assertThat(car2.getPosition()).isEqualTo(98);
        assertThat(car2.getSpeed()).isEqualTo(0);
    }
}
