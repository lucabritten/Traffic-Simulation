package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TrafficLightIntegrationTest {

    @Test
    void intersection_lightsChangeCorrectlyOnPhaseSwitch() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);
        Road r2 = new Road(i1,i2,100);

        Phase p1 = new Phase(Set.of(r1), 1, 1, 1);
        Phase p2 = new Phase(Set.of(r2), 1, 1, 1);

        FixedCycleStrategy strategy = new FixedCycleStrategy(List.of(p1, p2));
        PhaseController controller = new PhaseController(strategy);

        i1.setEntryLights(Map.of(
                r1, new TrafficLight(1),
                r2, new TrafficLight(2)
        ));

        // Tick 0 → p1 active
        controller.update(i1, 0);
        controller.applyPhase(i1);

        assertThat(i1.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.GREEN);
        assertThat(i1.getLightFor(r2).getState()).isEqualTo(TrafficLight.State.RED);

        // Tick through full phase duration (3 ticks)
        controller.update(i1, 1);
        controller.update(i1, 2);
        controller.update(i1, 3);

        controller.applyPhase(i1);

        assertThat(i1.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.RED);
        assertThat(i1.getLightFor(r2).getState()).isEqualTo(TrafficLight.State.GREEN);
    }
}
