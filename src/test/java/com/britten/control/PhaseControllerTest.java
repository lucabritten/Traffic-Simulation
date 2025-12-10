package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PhaseControllerTest {

    @Test
    void startsWithFirstPhase() {
        Phase p1 = new Phase(Set.of(), 5,3,2);
        Phase p2 = new Phase(Set.of(), 5,3,2);

        PhaseController controller = new PhaseController(List.of(p1,p2));

        assertThat(controller.getCurrentPhase()).isEqualTo(p1);
    }

    @Test
    void switchesPhaseAfterTotalDuration() {
        Phase p1 = new Phase(Set.of(), 3,2,1); // total = 6
        Phase p2 = new Phase(Set.of(), 3,2,1);

        PhaseController controller = new PhaseController(List.of(p1,p2));

        for (int i=0;i<6;i++)
            controller.update();

        assertThat(controller.getCurrentPhase()).isEqualTo(p2);
    }

    @Test
    void cyclesBackToFirstPhase() {
        Phase p1 = new Phase(Set.of(), 1,1,1);
        Phase p2 = new Phase(Set.of(), 1,1,1);

        PhaseController c = new PhaseController(List.of(p1,p2));

        for (int i=0;i<6;i++) c.update();

        assertThat(c.getCurrentPhase()).isEqualTo(p1);
    }

    @Test
    void greenYellowRedIntervalsAreCorrect() {
        Road r = new Road(new Intersection(1), new Intersection(2), 10);

        Phase p = new Phase(Set.of(r), 2,3,4);
        PhaseController c = new PhaseController(List.of(p));

        // GREEN ticks 0–1
        assertThat(c.getCurrentColorFor(r)).isEqualTo(TrafficLight.State.GREEN);
        c.update();
        assertThat(c.getCurrentColorFor(r)).isEqualTo(TrafficLight.State.GREEN);

        // YELLOW ticks 2–4
        c.update(); // tick 2
        assertThat(c.getCurrentColorFor(r)).isEqualTo(TrafficLight.State.YELLOW);

        for (int i=0;i<2;i++) {
            c.update();
            assertThat(c.getCurrentColorFor(r)).isEqualTo(TrafficLight.State.YELLOW);
        }

        // RED ticks 5–8
        for (int i=0;i<4;i++) {
            c.update();
            assertThat(c.getCurrentColorFor(r)).isEqualTo(TrafficLight.State.RED);
        }
    }
}
