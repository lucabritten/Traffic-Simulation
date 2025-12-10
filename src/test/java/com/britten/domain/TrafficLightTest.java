package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TrafficLightTest {

    @Test
    void defaultStateIsRed() {
        TrafficLight tl = new TrafficLight(1);
        assertThat(tl.getState()).isEqualTo(TrafficLight.State.RED);
    }

    @Test
    void setState_setsCorrectValue() {
        TrafficLight tl = new TrafficLight(1);
        tl.setState(TrafficLight.State.GREEN);
        assertThat(tl.getState()).isEqualTo(TrafficLight.State.GREEN);
    }

    @Test
    void stateChangesCorrectly() {
        TrafficLight tl = new TrafficLight(1);

        tl.setState(TrafficLight.State.GREEN);
        tl.setState(TrafficLight.State.YELLOW);
        tl.setState(TrafficLight.State.RED);

        assertThat(tl.getState()).isEqualTo(TrafficLight.State.RED);
    }


}
