package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;

import java.util.List;
import java.util.Map;

import static com.britten.domain.TrafficLight.State.GREEN;
import static com.britten.domain.TrafficLight.State.RED;

public class PhaseController {

    private List<Phase> phases;
    private int currentPhase = 0;
    private int tickInPhase = 0;

    public PhaseController(List<Phase> phases){
        this.phases = phases;
    }

    public void update() {
        tickInPhase++;
        if (tickInPhase >= phases.get(currentPhase).duration) {
            tickInPhase = 0;
            currentPhase = (currentPhase + 1) % phases.size();
        }
    }

    public Phase getCurrentPhase(){
        return phases.get(currentPhase);
    }

    public void applyPhase(Intersection intersection) {
        Phase phase = getCurrentPhase();

        for (Map.Entry<Road, TrafficLight> e : intersection.getEntryLights().entrySet()) {
            Road r = e.getKey();
            TrafficLight light = e.getValue();

            if (phase.greenRoads.contains(r)) {
                light.setState(GREEN);
            } else {
                light.setState(RED);
            }
        }
    }
}
