package com.britten.domain;

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
    private SimulationEventPublisher publisher;

    public TrafficLight(int id){
        this.id = id;
        state = State.RED;
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

    @Override
    public String toString() {
        return "TrafficLight{" +
                "state=" + state +
                ", currentTick=" + currentTick +
                '}';
    }
}
