package com.britten.control;

import com.britten.domain.Road;

import java.util.HashSet;
import java.util.Set;

public class Phase {
    private Set<Road> permittedFlows;
    int greenDuration;
    int yellowDuration;
    int redDuration;

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

    public Set<Road> getPermittedFlows() {
        return permittedFlows;
    }
}
