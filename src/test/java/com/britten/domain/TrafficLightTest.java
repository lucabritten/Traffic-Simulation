package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrafficLightTest {

    @Test
    public void testInitializeObjectWithValidFieldData(){

        int greenDuration = 1;
        int yellowDuration = 1;
        int redDuration = 1;
        int cycleTics = greenDuration + yellowDuration + redDuration;
        TrafficLight.State initState = TrafficLight.State.RED;

        TrafficLight trafficLight = new TrafficLight(greenDuration,yellowDuration,redDuration);

        assertThat(trafficLight.getGreenDuration()).isEqualTo(greenDuration);
        assertThat(trafficLight.getYellowDuration()).isEqualTo(yellowDuration);
        assertThat(trafficLight.getRedDuration()).isEqualTo(redDuration);
        assertThat(trafficLight.getCylceTics()).isEqualTo(cycleTics);
        assertThat(trafficLight.getState()).isEqualTo(initState);
    }

    @Test
    public void testInitializeObjectWithInvalidGreenDuration(){

        int greenDuration = -1;
        int yellowDuration = 1;
        int redDuration = 1;

        assertThatThrownBy(() -> new TrafficLight(greenDuration,yellowDuration,redDuration))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Traffic Light duration cannot be negative or zero.");
    }
}
