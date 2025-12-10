package com.britten.control;

import com.britten.domain.Road;
import com.britten.domain.TrafficLight;

import java.util.HashSet;
import java.util.Set;

public class Phase {
    private Set<Road> permittedFlows;
    private int greenDuration;
    private int yellowDuration;
    private int redDuration;

    public Phase(Set<Road> permittedFlows, int greenDuration, int yellowDuration, int redDuration){
        if(permittedFlows == null)
            throw new IllegalArgumentException("Permitted flows are not allowed to be null!");
        if(greenDuration <= 0 || yellowDuration <= 0 || redDuration <= 0)
            throw new IllegalArgumentException("Duration is not allowed to be negative or zero!");

        this.permittedFlows = new HashSet<>(permittedFlows);
        this.redDuration = redDuration;
        this.yellowDuration = yellowDuration;
        this.greenDuration = greenDuration;
    }

    public Phase(Set<Road> permittedFlows, int duration) {
        this(permittedFlows, duration, duration, duration);
    }



    public int totalDuration(){
        return greenDuration + yellowDuration + redDuration;
    }

    /**
     * Returns the traffic light state for the given road depending on the elapsed ticks
     * inside the currently active phase. elapsedInPhase must be between 0 and totalDuration()-1.
     */
    public TrafficLight.State getStateForRoad(Road road, int elapsedInPhase) {
        if (!permittedFlows.contains(road)) {
            return TrafficLight.State.RED;
        }

        if (elapsedInPhase < greenDuration) {
            return TrafficLight.State.GREEN;
        }

        if (elapsedInPhase < greenDuration + yellowDuration) {
            return TrafficLight.State.YELLOW;
        }

        return TrafficLight.State.RED;
    }

    public Set<Road> getPermittedFlows() {
        return permittedFlows;
    }

}
