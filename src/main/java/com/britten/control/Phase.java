package com.britten.control;

import com.britten.domain.Road;

import java.util.Set;

public class Phase {
    Set<Road> greenRoads;
    int duration;

    public Phase(Set greenRoads, int duration){
        this.greenRoads = greenRoads;
        this.duration = duration;
    }
}
