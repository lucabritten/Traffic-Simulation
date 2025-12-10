package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;

import java.util.List;
import java.util.Map;

import static com.britten.domain.TrafficLight.State.GREEN;
import static com.britten.domain.TrafficLight.State.RED;

public class PhaseController {

    private List<Phase> signalPlan;
    private int phaseIndex = 0;
    private int phaseElapsedTicks = 0;

    public PhaseController(List<Phase> signalPlan){
        this.signalPlan = signalPlan;
    }


    public TrafficLight.State getCurrentColorFor(Road road) {
        Phase phase = getCurrentPhase();
        int tick = phaseElapsedTicks;

        if (!phase.getPermittedFlows().contains(road))
            return TrafficLight.State.RED;

        if (tick < phase.greenDuration)
            return TrafficLight.State.GREEN;
        else if (tick < phase.greenDuration + phase.yellowDuration)
            return TrafficLight.State.YELLOW;
        else
            return TrafficLight.State.RED;
    }

    public void update() {
        phaseElapsedTicks++;
        if (phaseElapsedTicks >= signalPlan.get(phaseIndex).totalDuration()) {
            phaseElapsedTicks = 0;
            phaseIndex = (phaseIndex + 1) % signalPlan.size();
        }
    }

    public Phase getCurrentPhase(){
        return signalPlan.get(phaseIndex);
    }

    public void applyPhase(Intersection intersection) {
        Phase phase = getCurrentPhase();

        for (Map.Entry<Road, TrafficLight> e : intersection.getEntryLights().entrySet()) {
            Road r = e.getKey();
            TrafficLight light = e.getValue();

            if (phase.getPermittedFlows().contains(r)) {
                light.setState(GREEN);
            } else {
                light.setState(RED);
            }
        }
    }
}
