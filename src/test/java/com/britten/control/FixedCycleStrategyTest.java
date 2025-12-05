package com.britten.control;

import com.britten.domain.TrafficLight;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FixedCycleStrategyTest {

    @Test
    public void testInitializeObjectWithInvalidGreenDuration(){

        int greenDuration = -1;
        int yellowDuration = 1;
        int redDuration = 1;

        assertThatThrownBy(() -> new FixedCycleStrategy(greenDuration,yellowDuration,redDuration))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Traffic Light duration cannot be negative or zero.");
    }

    @Test
    public void testTrafficLightUpdate_stateShouldSwitchFromRedToGreen(){
        int greenDuration = 5;
        int yellowDuration = 1;
        int redDuration = 10;

        TrafficLight trafficLight = new TrafficLight(new FixedCycleStrategy(greenDuration,yellowDuration,redDuration));

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

        TrafficLight trafficLight = new TrafficLight(new FixedCycleStrategy(greenDuration,yellowDuration,redDuration));

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

        TrafficLight trafficLight = new TrafficLight(new FixedCycleStrategy(greenDuration,yellowDuration,redDuration));

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
