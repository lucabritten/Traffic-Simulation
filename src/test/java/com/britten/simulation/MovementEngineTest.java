package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MovementEngineTest {

    private MovementEngine engine;

    private Intersection i1;
    private Intersection i2;
    private Road road;
    private Vehicle vehicle;

    @BeforeEach
    void setup() {
        engine = new MovementEngine();

        i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(2,2,2)));
        i2 = new Intersection(2, new TrafficLight(new FixedCycleStrategy(2,2,2)));

        road = new Road(i1, i2, 100);

        i1.addOutgoingRoad(road);

        vehicle = new Car(1, road, 10);
        vehicle.setPosition(50);
    }

    private Snapshot snapshotWithGreen() {
        Map<Road, List<Vehicle>> roadMap = new HashMap<>();
        roadMap.put(road, List.of(vehicle));

        Map<Road, String> lightMap = new HashMap<>();
        lightMap.put(road, "GREEN");

        return new Snapshot(roadMap, lightMap);
    }

    private Snapshot snapshotWithRed() {
        Map<Road, List<Vehicle>> roadMap = new HashMap<>();
        roadMap.put(road, List.of(vehicle));

        Map<Road, String> lightMap = new HashMap<>();
        lightMap.put(road, "RED");

        return new Snapshot(roadMap, lightMap);
    }

    @Test
    void computeNext_movesForwardNormally() {
        Snapshot snap = snapshotWithGreen();

        engine.computeNext(vehicle, snap, 1);

        assertThat(vehicle.getNextPosition()).isEqualTo(60);
        assertThat(vehicle.getNextRoad()).isEqualTo(road);
    }

    @Test
    void computeNext_stopsAtEndWhenRed() {
        vehicle.setPosition(95);

        Snapshot snap = snapshotWithRed();

        engine.computeNext(vehicle, snap, 10);

        assertThat(vehicle.getNextPosition()).isEqualTo(100);
        assertThat(vehicle.getNextRoad()).isEqualTo(road);
    }

    @Test
    void computeNext_movesToNextRoadOnGreen() {
        Road next = new Road(i2, i1, 100);

        i2.addOutgoingRoad(next);

        vehicle.setNextRoad(next);
        vehicle.setPosition(95);


        Snapshot snap = snapshotWithGreen();

        engine.computeNext(vehicle, snap, 1);

        assertThat(vehicle.getNextRoad()).isEqualTo(next);
        assertThat(vehicle.getNextPosition()).isEqualTo(5);
    }
}
