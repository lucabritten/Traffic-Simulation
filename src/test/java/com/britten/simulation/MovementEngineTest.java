package com.britten.simulation;

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

        i1 = new Intersection(1);
        i2 = new Intersection(2);

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

    @Test
    void yellowPhaseTooClose_vehicleStops() {
        Road r = new Road(new Intersection(1), new Intersection(2), 100);
        Vehicle v = new Car(1, r, 10);
        v.setPosition(98); // close to end

        TrafficLight yellow = new TrafficLight(1);
        yellow.setState(TrafficLight.State.YELLOW);

        Intersection i2 = r.getTo();
        i2.addIncomingRoad(r, yellow);

        MovementEngine engine = new MovementEngine();
        Snapshot snapshot = new SnapshotBuilder().withVehicle(v).onRoad(r).build();

        engine.computeNext(v, snapshot, 1);

        assertThat(v.getNextSpeed()).isZero();
        assertThat(v.getNextPosition()).isEqualTo(100);
    }

    @Test
    void yellowPhaseFarEnough_vehicleContinues() {
        Road r = new Road(new Intersection(1), new Intersection(2), 100);
        Vehicle v = new Car(1, r, 10);
        v.setPosition(50);

        TrafficLight yellow = new TrafficLight(1);
        yellow.setState(TrafficLight.State.YELLOW);

        r.getTo().addIncomingRoad(r, yellow);

        MovementEngine engine = new MovementEngine();
        Snapshot snapshot = SnapshotBuilder.of(v, r);

        engine.computeNext(v, snapshot, 1);

        assertThat(v.getNextPosition()).isEqualTo(60);
    }

    @Test
    void greenPhase_ignoreYellowLogic() {
        Road r = new Road(new Intersection(1), new Intersection(2), 100);
        Vehicle v = new Car(1, r, 10);

        TrafficLight green = new TrafficLight(1);
        green.setState(TrafficLight.State.GREEN);

        r.getTo().addIncomingRoad(r, green);

        MovementEngine engine = new MovementEngine();
        Snapshot s = SnapshotBuilder.of(v, r);

        engine.computeNext(v, s, 1);

        assertThat(v.getNextPosition()).isEqualTo(10);
    }
}
