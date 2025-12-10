package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;

import java.util.Map;

public class PhaseController {

    private final PhaseStrategy strategy;
    private Phase currentPhase;
    private int elapsedInPhase = 0;

    public PhaseController(PhaseStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Called every tick by SimulationEngine.
     * Strategy decides which phase should be active.
     */
    public void update(Intersection intersection, int tick) {
        Phase next = strategy.chooseNextPhase(intersection, tick);
        if (next == null) {
            this.currentPhase = null;
            this.elapsedInPhase = 0;
            return;
        }

        if (currentPhase != next) {
            this.currentPhase = next;
            this.elapsedInPhase = 0;
        } else {
            this.elapsedInPhase++;
        }
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Applies the current phase to all traffic lights of the intersection.
     */
    public void applyPhase(Intersection intersection) {
        if (currentPhase == null)
            return;

        for (Map.Entry<Road, TrafficLight> entry : intersection.getEntryLights().entrySet()) {
            Road road = entry.getKey();
            TrafficLight light = entry.getValue();

            int elapsed = Math.max(0, Math.min(elapsedInPhase, currentPhase.totalDuration() - 1));
            TrafficLight.State state = currentPhase.getStateForRoad(road, elapsed);
            light.setState(state);
        }
    }
}
