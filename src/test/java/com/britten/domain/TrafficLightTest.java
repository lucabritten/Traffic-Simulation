package com.britten.domain;

import com.britten.control.FixedCycleStrategy;
import com.britten.control.TrafficLightStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TrafficLightTest {

//    @Test
//    public void testInitializeObjectWithValidFieldData(){
//
//        int greenDuration = 1;
//        int yellowDuration = 1;
//        int redDuration = 1;
//        TrafficLightStrategy strategy = new FixedCycleStrategy(greenDuration,yellowDuration, redDuration);
//        TrafficLight.State initState = TrafficLight.State.RED;
//
//        TrafficLight trafficLight = new TrafficLight(strategy);
//
//        assertThat(trafficLight.getCurrentTick()).isZero();
//        assertThat(trafficLight.getStrategy()).isEqualTo(strategy);
//        assertThat(trafficLight.getState()).isEqualTo(initState);
//    }
//
//    @Test
//    public void testInitializeObjectWithInvalidGreenDuration(){
//
//        assertThatThrownBy(() -> new TrafficLight(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Strategy is not allowed to be null.");
//    }


}
