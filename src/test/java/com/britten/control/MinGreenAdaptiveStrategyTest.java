package com.britten.control;

import com.britten.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MinGreenAdaptiveStrategyTest {

    private Intersection i1;
    private Intersection i2;
    private Road r1;
    private Road r2;
    TrafficLight tl1;
    TrafficLight tl2;
    private Phase p1;
    private Phase p2;
    private MinGreenAdaptiveStrategy strategy;

    @BeforeEach
    void setUp() {
        i1 = new Intersection(1);
        i2 = new Intersection(2);

        r1 = new Road(i1, i2, 100);
        r2 = new Road(i2, i1, 100);

        tl1 = new TrafficLight(1);
        tl2 = new TrafficLight(2);

        i2.addIncomingRoad(r1, tl1);
        i1.addIncomingRoad(r2, tl2);

        p1 = new Phase(Set.of(r1), 3, 1, 1);
        p2 = new Phase(Set.of(r2), 3, 1, 1);

        strategy = new MinGreenAdaptiveStrategy(List.of(p1, p2));
    }

    @Test
    void staysInSamePhase_duringMinGreen() {
        Phase first = strategy.chooseNextPhase(i2, 0);
        Phase second = strategy.chooseNextPhase(i2, 1);
        Phase third = strategy.chooseNextPhase(i2, 2);

        assertThat(first).isSameAs(p1);
        assertThat(second).isSameAs(p1);
        assertThat(third).isSameAs(p1);
    }

    @Test
    void staysInPhase_afterMinGreen_whenQueueExists() {
        Vehicle v = new Car(1, r1, 10);
        v.setSpeed(0);
        v.setPosition(95);
        r1.addVehicle(v);

        // MinGreen erreichen
        strategy.chooseNextPhase(i2, 0);
        strategy.chooseNextPhase(i2, 1);
        strategy.chooseNextPhase(i2, 2);

        Phase next = strategy.chooseNextPhase(i2, 3);

        assertThat(next).isSameAs(p1);
    }

    @Test
    void switchesPhase_afterMinGreen_whenNoQueue(){
        strategy.chooseNextPhase(i2, 0);
        strategy.chooseNextPhase(i2, 1);
        strategy.chooseNextPhase(i2, 2);

        Phase next = strategy.chooseNextPhase(i2, 3);

        assertThat(next).isSameAs(p2);
    }

    @Test
    void cyclesBackToFirstPhase_afterLastPhase() {
        // Wechsel zu p2
        for(int i = 0; i < 4; i++) {
            strategy.chooseNextPhase(i2, i);
        }

        strategy.chooseNextPhase(i2, 5);
        strategy.chooseNextPhase(i2, 6);
        strategy.chooseNextPhase(i2, 7);

        Phase next = strategy.chooseNextPhase(i2, 8);

        assertThat(next).isSameAs(p1);
    }

    @Test
    void emptyQueue_doesNotPreventPhaseSwitch() {
        Phase next = null;

        for(int i = 0; i < 10; i++) {
            next = strategy.chooseNextPhase(i2, i);
        }

        assertThat(next).isNotNull();
    }


}
