package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.simulation.Snapshot;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PhaseSnapshotTest {

    @Test
    void snapshotReflectsTrafficLightStateCorrectly() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r = new Road(i1, i2, 100);
        TrafficLight light = new TrafficLight(1);
        light.setState(TrafficLight.State.GREEN);

        Snapshot s = new Snapshot(
                Map.of(r, List.of()),
                Map.of(r, light.getState().name()),
                5
        );

        assertThat(s.getLightStates().get(r)).isEqualTo("GREEN");
        assertThat(s.getTick()).isEqualTo(5);
    }
}
