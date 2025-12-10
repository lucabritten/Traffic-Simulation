package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PhaseTest {

    private Phase phase;
    private Road road;

    @BeforeEach
    void setUp(){
        road = new Road(
                new Intersection(1),
                new Intersection(2),
                10
        );
        phase = new Phase(Set.of(road), 10);
    }
    //Constructor sets fields correctly
    @Test
    void constructor_validArgs_setValuesCorrectly(){
        phase = new Phase(Set.of(new Road(
                new Intersection(1),
                new Intersection(2),1)
        ), 10);

        assertThat(phase.getPermittedFlows()).hasSize(1);
        assertThat(phase.totalDuration()).isEqualTo(30);
    }

    @Test
    void constructor_emptySet_isAccepted(){
        phase = new Phase(Set.of(), 10);

        assertThat(phase.getPermittedFlows()).isNotNull();
        assertThat(phase.getPermittedFlows()).hasSize(0);
        assertThat(phase.totalDuration()).isEqualTo(30);
    }

    @Test
    void constructor_nullSet_throwsException(){
        assertThatThrownBy(() ->
                new Phase(null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Permitted flows are not allowed to be null!");
    }

    //totalDuration = r + y + g
    @Test
    void totalDuration_sumIsEqualToPhaseDurations(){
        assertThat(phase.totalDuration()).isEqualTo(30);
    }

    //permittedFlows includes correct roads
    @Test
    void permittedFlows_containsExpectedRoad(){
        assertThat(phase.getPermittedFlows()).contains(road);
    }

    @Test
    void constructor_negativeDuration_throwsException(){
        assertThatThrownBy( () ->
                new Phase(Set.of(road), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration is not allowed to be negative or zero!");
    }

    @Test
    void constructor_zeroDuration_throwsException(){
        assertThatThrownBy( () ->
                new Phase(Set.of(road), 0,2,-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration is not allowed to be negative or zero!");
    }
}
