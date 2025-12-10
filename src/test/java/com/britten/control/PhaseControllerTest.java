package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PhaseControllerTest {

    @Test
    void controller_appliesCorrectGreenYellowRedToLights() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);
        Road r2 = new Road(i2, i1, 100);

        Phase p = new Phase(Set.of(r1), 2, 1, 1);
        FixedCycleStrategy strategy = new FixedCycleStrategy(List.of(p));
        PhaseController controller = new PhaseController(strategy);

        i1.setEntryLights(Map.of(
                r1, new TrafficLight(1),
                r2, new TrafficLight(2)
        ));

        // First tick → p is active, elapsed=1
        controller.update(i1, 0);
        controller.applyPhase(i1);

        assertThat(i1.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.GREEN);
        assertThat(i1.getLightFor(r2).getState()).isEqualTo(TrafficLight.State.RED);
    }

    @Test
    void controller_updatesPhaseBeforeApplyingLights() {
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2, 100);

        Phase p = new Phase(Set.of(r1), 1, 1, 1);
        FixedCycleStrategy strategy = new FixedCycleStrategy(List.of(p));
        PhaseController controller = new PhaseController(strategy);

        i2.setEntryLights(Map.of(r1, new TrafficLight(1)));

        controller.update(i2, 0);
        controller.applyPhase(i2);

        assertThat(i2.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.GREEN);
    }

    @Test
    void controller_doesNothingIfCurrentPhaseNull() {
        Intersection i1 = new Intersection(1);

        Road r1 = new Road(i1, new Intersection(2), 100);
        i1.setEntryLights(Map.of(r1, new TrafficLight(1)));

        PhaseController controller = new PhaseController((inter, t) -> null);
        controller.update(i1, 0);
        controller.applyPhase(i1);

        // should remain RED
        assertThat(i1.getLightFor(r1).getState()).isEqualTo(TrafficLight.State.RED);
    }
}
