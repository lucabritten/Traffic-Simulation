package com.britten.control;

import com.britten.domain.TrafficLight;
import com.britten.logging.SimulationEventListener;
import com.britten.logging.SimulationEventPublisher;

public class FixedCycleStrategy implements TrafficLightStrategy {

    private final int greenDuration;
    private final int yellowDuration;
    private final int redDuration;

    private SimulationEventPublisher publisher;

    public FixedCycleStrategy(int greenDuration, int yellowDuration, int redDuration){
        if(greenDuration <= 0 || yellowDuration <= 0 || redDuration < 0)
            throw new IllegalArgumentException("Traffic Light duration cannot be negative or zero.");

        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
    }

    public void setPublisher(SimulationEventPublisher publisher){
        this.publisher = publisher;
    }

    @Override
    public TrafficLight.State nextState(TrafficLight light) {

        TrafficLight.State current = light.getState();
        int currentTick = light.getCurrentTick();

        switch (current){
            case RED -> {
                if(currentTick >= redDuration) {
                    light.resetTick();
                    return TrafficLight.State.GREEN;
                }
            }
            case GREEN -> {
                if(currentTick >= greenDuration){
                    light.resetTick();
                    return TrafficLight.State.YELLOW;
                }
            }
            case YELLOW -> {
                if(currentTick >= yellowDuration){
                    light.resetTick();
                    return TrafficLight.State.RED;
                }
            }
        }
        return current;
    }

    @Override
    public String toString() {
        return "FixedCycleStrategy{" +
                "greenDuration=" + greenDuration +
                ", yellowDuration=" + yellowDuration +
                ", redDuration=" + redDuration +
                '}';
    }
}
