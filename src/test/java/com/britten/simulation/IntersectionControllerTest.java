package com.britten.simulation;

import com.britten.domain.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntersectionControllerTest {


    private IntersectionController controller;

    private Intersection fromIntersection;
    private Intersection toIntersection;
    private Road road;
    private Vehicle v1;
    private Vehicle v2;


    @Test
    void redLight_vehicleMayNotEnter() {
        Snapshot s = SnapshotBuilder.singleRoadSnapshot("RED");
        Road road = s.getRoadVehicles().keySet().iterator().next();
        Vehicle v = s.getFrontVehicle(road);
        IntersectionController c = new IntersectionController();

        assertThat(c.mayEnter(v, road, s))
                .isFalse();
    }

    @Test
    void green_firstVehicleMayEnter() {
        Snapshot s = SnapshotBuilder.singleRoadSnapshot("GREEN");
        Road road = s.getRoadVehicles().keySet().iterator().next();
        Vehicle v = s.getFrontVehicle(road);
        IntersectionController c = new IntersectionController();

        assertThat(c.mayEnter(v, road, s))
                .isTrue();
    }

    @Test
    void green_secondVehicleMayNotEnter() {
        Snapshot s = SnapshotBuilder.twoVehicleSnapshot("GREEN");
        Road road = s.getRoadVehicles().keySet().iterator().next();
        Vehicle v = s.getSecondVehicle(road);
        IntersectionController c = new IntersectionController();

        assertThat(c.mayEnter(v, road, s))
                .isFalse();
    }

    @Test
    void yellow_onlyMovingFrontVehicleMayEnter() {
        Snapshot s = SnapshotBuilder.movingFrontVehicle("YELLOW");
        Road road = s.getRoadVehicles().keySet().iterator().next();
        Vehicle v = s.getFrontVehicle(road);
        IntersectionController c = new IntersectionController();

        assertThat(c.mayEnter(v, road, s))
                .isTrue();
    }

    @Test
    void yellow_standingFrontVehicleMayNotEnter() {
        Snapshot s = SnapshotBuilder.stoppedFrontVehicle("YELLOW");
        Road road = s.getRoadVehicles().keySet().iterator().next();
        Vehicle v = s.getFrontVehicle(road);
        IntersectionController c = new IntersectionController();

        assertThat(c.mayEnter(v, road, s))
                .isFalse();
    }
}
