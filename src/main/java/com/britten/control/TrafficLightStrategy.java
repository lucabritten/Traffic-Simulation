package com.britten.control;

import com.britten.domain.TrafficLight;

public interface TrafficLightStrategy {
    TrafficLight.State nextState(TrafficLight light);


}
