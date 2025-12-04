package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(trafficLight.getCylceTicks()).isEqualTo(cycleTics);
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

    @Test
    public void testTrafficLightUpdate_stateShouldSwitchFromRedToGreen(){
        int greenDuration = 5;
        int yellowDuration = 1;
        int redDuration = 10;

        TrafficLight trafficLight = new TrafficLight(greenDuration,yellowDuration,redDuration);

        int counter = 0;
        while(counter < redDuration - 1){
            counter++;
            trafficLight.update();
        }

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.RED);

        trafficLight.update();

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.GREEN);
    }

    @Test
    public void testTrafficLightUpdate_stateShouldSwitchFromGreenToYellow(){
        int greenDuration = 5;
        int yellowDuration = 1;
        int redDuration = 10;

        TrafficLight trafficLight = new TrafficLight(greenDuration,yellowDuration,redDuration);



        int counter = 0;
        while(counter < redDuration + greenDuration - 1){
            counter++;
            trafficLight.update();
        }

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.GREEN);

        trafficLight.update();

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.YELLOW);
    }

    @Test
    public void testTrafficLightUpdate_stateShouldSwitchFromYellowToRed(){
        int greenDuration = 5;
        int yellowDuration = 1;
        int redDuration = 10;

        TrafficLight trafficLight = new TrafficLight(greenDuration,yellowDuration,redDuration);



        int counter = 0;
        while(counter < redDuration + greenDuration + yellowDuration - 1){
            counter++;
            trafficLight.update();
        }

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.YELLOW);

        trafficLight.update();

        assertThat(trafficLight.getState()).isEqualTo(TrafficLight.State.RED);
    }
}
