package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class IntersectionControllerTest {


    private IntersectionController controller;

    private Intersection fromIntersection;
    private Intersection toIntersection;
    private Road road;
    private Vehicle v1;
    private Vehicle v2;

    @BeforeEach
    void setup() {
        controller = new IntersectionController();

        fromIntersection = new Intersection(1);
        toIntersection = new Intersection(2);

        road = new Road(fromIntersection, toIntersection, 100);

        v1 = new Car(1, road, 10);
        v1.setPosition(50);

        v2 = new Car(2, road, 10);
        v2.setPosition(40);
    }

    private Snapshot snapshotWith(String light, List<Vehicle> vehicles) {
        Map<Road, List<Vehicle>> roadMap = new HashMap<>();
        roadMap.put(road, vehicles);

        Map<Road, String> lightMap = new HashMap<>();
        lightMap.put(road, light);

        return new Snapshot(roadMap, lightMap);
    }

    @Test
    void mayEnter_green_and_firstVehicle_returnsTrue() {
        Snapshot snap = snapshotWith("GREEN", List.of(v1, v2));

        boolean result = controller.mayEnter(v1, road, snap);

        assertThat(result).isTrue();
    }

    @Test
    void mayEnter_green_but_not_firstVehicle_returnsFalse() {
        Snapshot snap = snapshotWith("GREEN", List.of(v1, v2));

        boolean result = controller.mayEnter(v2, road, snap);

        assertThat(result).isFalse();
    }

    @Test
    void mayEnter_red_light_blocksAll() {
        Snapshot snap = snapshotWith("RED", List.of(v1));

        boolean result = controller.mayEnter(v1, road, snap);

        assertThat(result).isFalse();
    }

    @Test
    void mayEnter_emptyRoad_green_allowsEnter() {
        Snapshot snap = snapshotWith("GREEN", Collections.emptyList());

        boolean result = controller.mayEnter(v1, road, snap);

        assertThat(result).isTrue();
    }
}
