package com.britten.control;

import com.britten.domain.Road;

import java.util.Set;

public class Phase {
    Set<Road> greenRoads;
    int duration;

    public Phase(Set phases, int duration){
        this.greenRoads = phases;
        this.duration = duration;
    }
}
