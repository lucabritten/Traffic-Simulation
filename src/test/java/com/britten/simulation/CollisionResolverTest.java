package com.britten.simulation;

import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CollisionResolverTest {


    private CollisionResolver resolver;
    private Intersection from;
    private Intersection to;
    private Road road;
    private Vehicle v1;
    private Vehicle v2;

    @BeforeEach
    void setup() {
        resolver = new CollisionResolver();

        from = new Intersection(1);
        to = new Intersection(2);

        road = new Road(from, to, 200);

        to.addIncomingRoad(road, new TrafficLight(1));

        v1 = new Car(1, road, 10);
        v1.setPosition(89);
        v1.setNextPosition(99);

        v2 = new Car(2, road, 11);
        v2.setPosition(87);
        v2.setNextPosition(98); // would collide with v1 if not fixed
    }

    private Snapshot snapshot(List<Vehicle> ordered) {
        Map<Road, List<Vehicle>> rm = new HashMap<>();
        rm.put(road, ordered);

        Map<Road, String> lights = new HashMap<>();
        lights.put(road, "GREEN");

        return new Snapshot(rm, lights);
    }

    @Test
    void resolver_enforcesMinimumDistance() {
        Snapshot snap = snapshot(List.of(v1, v2));

        resolver.resolve(snap, List.of(v1, v2));

        assertThat(v2.getNextPosition())
                .isEqualTo(v1.getNextPosition() - 2);
    }

    @Test
    void resolver_doesNotMoveFrontVehicle() {
        Snapshot snap = snapshot(List.of(v1, v2));

        resolver.resolve(snap, List.of(v1, v2));

        assertThat(v1.getNextPosition()).isEqualTo(99);
    }
}
