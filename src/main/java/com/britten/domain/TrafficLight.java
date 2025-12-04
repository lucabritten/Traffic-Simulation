package com.britten.domain;

public class TrafficLight {

    enum State {
        RED, YELLOW, GREEN
    }

    private State state;
    private int greenDuration;
    private int yellowDuration;
    private int redDuration;
    private int cylceTics;

    public TrafficLight(int greenDuration, int yellowDuration, int redDuration){
        if(greenDuration <= 0 || yellowDuration <= 0 || redDuration < 0)
            throw new IllegalArgumentException("Traffic Light duration cannot be negative or zero.");

        state = State.RED;
        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
        cylceTics = greenDuration + yellowDuration + redDuration;
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

    public int getCylceTics() {
        return cylceTics;
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "state=" + state +
                ", greenDuration=" + greenDuration +
                ", yellowDuration=" + yellowDuration +
                ", redDuration=" + redDuration +
                ", cylceTics=" + cylceTics +
                '}';
    }
}
