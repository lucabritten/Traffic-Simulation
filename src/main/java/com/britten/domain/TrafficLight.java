package com.britten.domain;

import com.britten.control.TrafficLightStrategy;

public class TrafficLight {

    public enum State {
        RED, YELLOW, GREEN
    }

    private State state;
    private int currentTick;
    private TrafficLightStrategy strategy;

    public TrafficLight(TrafficLightStrategy strategy){
        if(strategy == null)
            throw new IllegalArgumentException("Strategy is not allowed to be null.");

        state = State.RED;
        this.strategy = strategy;
        currentTick = 0;
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

    public void update(){
        currentTick++;
        state = strategy.nextState(this);
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
