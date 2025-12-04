package com.britten.domain;

public class TrafficLight {

    enum State {
        RED, YELLOW, GREEN
    }

    private State state;
    private int greenDuration;
    private int yellowDuration;
    private int redDuration;
    private int cylceTicks;
    private int currentTick;

    public TrafficLight(int greenDuration, int yellowDuration, int redDuration){
        if(greenDuration <= 0 || yellowDuration <= 0 || redDuration < 0)
            throw new IllegalArgumentException("Traffic Light duration cannot be negative or zero.");

        state = State.RED;
        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
        cylceTicks = greenDuration + yellowDuration + redDuration;
        currentTick = 0;
    }

    public State getState() {
        return state;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public int getYellowDuration() {
        return yellowDuration;
    }

    public int getRedDuration() {
        return redDuration;
    }

    public int getCylceTicks() {
        return cylceTicks;
    }

    public void update(){
        currentTick++;

        if(state == State.RED && currentTick >= redDuration){
            state = State.GREEN;
            currentTick = 0;
        } else if(state == State.GREEN && currentTick >= greenDuration) {
            state = State.YELLOW;
            currentTick = 0;
        } else if(state == State.YELLOW && currentTick >= yellowDuration){
            state = State.RED;
            currentTick = 0;
        }
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "state=" + state +
                ", greenDuration=" + greenDuration +
                ", yellowDuration=" + yellowDuration +
                ", redDuration=" + redDuration +
                ", cylceTics=" + cylceTicks +
                '}';
    }
}
