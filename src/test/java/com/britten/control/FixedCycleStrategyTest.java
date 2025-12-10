package com.britten.control;

import com.britten.domain.Intersection;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FixedCycleStrategyTest {

    @Test
    void strategy_startsAtPhase0() {
        Phase p1 = new Phase(Set.of(), 3, 2, 4);
        Phase p2 = new Phase(Set.of(), 5, 3, 2);

        FixedCycleStrategy s = new FixedCycleStrategy(List.of(p1, p2));
        Phase result = s.chooseNextPhase(new Intersection(1), 0);

        assertThat(result).isEqualTo(p1);
    }

    @Test
    void strategy_switchesPhaseAfterTotalDuration() {
        Phase p1 = new Phase(Set.of(), 1, 1, 1);
        Phase p2 = new Phase(Set.of(), 2, 1, 1);

        FixedCycleStrategy s = new FixedCycleStrategy(List.of(p1, p2));
        Intersection dummy = new Intersection(1);

        // total duration = 3 → after 3 calls it must switch
        s.chooseNextPhase(dummy, 0);
        s.chooseNextPhase(dummy, 1);
        Phase stillP1 = s.chooseNextPhase(dummy, 2);

        assertThat(stillP1).isEqualTo(p1);

        Phase nowP2 = s.chooseNextPhase(dummy, 3);
        assertThat(nowP2).isEqualTo(p2);
    }

    @Test
    void strategy_cyclesBackToPhase0() {
        Phase p1 = new Phase(Set.of(), 1, 1, 1);
        Phase p2 = new Phase(Set.of(), 1, 1, 1);

        FixedCycleStrategy s = new FixedCycleStrategy(List.of(p1, p2));
        Intersection dummy = new Intersection(1);

        // Move through all phases
        s.chooseNextPhase(dummy, 0);
        s.chooseNextPhase(dummy, 1);
        s.chooseNextPhase(dummy, 2);
        s.chooseNextPhase(dummy, 3);
        s.chooseNextPhase(dummy, 4);
        s.chooseNextPhase(dummy, 5);


        Phase backToP1 = s.chooseNextPhase(dummy, 6);
        assertThat(backToP1).isEqualTo(p1);
    }

    @Test
    void elapsedTicks_incrementsCorrectly() {
        Phase p1 = new Phase(Set.of(), 2, 2, 1);
        FixedCycleStrategy s = new FixedCycleStrategy(List.of(p1));

        Intersection dummy = new Intersection(1);

        s.chooseNextPhase(dummy, 0);
        assertThat(s.getElapsedTicks()).isEqualTo(1);

        s.chooseNextPhase(dummy, 1);
        assertThat(s.getElapsedTicks()).isEqualTo(2);
    }
}
