package com.britten.domain;

import com.britten.control.TrafficLightStrategy;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventPublisher;

import java.util.Map;

public class TrafficLight {

    public enum State {
        RED, YELLOW, GREEN
    }
    private int id;
    private State state;
    private int currentTick;
    private TrafficLightStrategy strategy;
    private SimulationEventPublisher publisher;

    public TrafficLight(int id,TrafficLightStrategy strategy){
        if(strategy == null)
            throw new IllegalArgumentException("Strategy is not allowed to be null.");

        this.id = id;
        state = State.RED;
        this.strategy = strategy;
        currentTick = 0;
    }

    public int getId(){
        return id;
    }

    public void setPublisher(SimulationEventPublisher publisher){
        this.publisher = publisher;
    }

    public State getState() {
        return state;
    }

    public void setState(State state){
        this.state = state;
    }

    public int getCurrentTick(){
        return currentTick;
    }

    public void resetTick(){
        this.currentTick = 0;
    }


    public void setStrategy(TrafficLightStrategy strategy){
        this.strategy = strategy;
        resetTick();
    }

    public TrafficLightStrategy getStrategy(){
        return this.strategy;
    }

    public void update(int tick){
        TrafficLight.State oldState = state;

        currentTick++;
        state = strategy.nextState(this);

        if (publisher != null && oldState != state) {
            publisher.publish(
                    new SimulationEvent(
                            "TRAFFICLIGHT_SWITCHED",
                            tick,
                            Map.of(
                                    "intersectionId", this.hashCode(),
                                    "from", oldState,
                                    "to", state
                            )
                    )
            );
        }
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "state=" + state +
                ", currentTick=" + currentTick +
                ", strategy=" + strategy +
                '}';
    }
}
