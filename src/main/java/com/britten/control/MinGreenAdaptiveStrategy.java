package com.britten.control;

import com.britten.domain.Intersection;
import com.britten.domain.Road;

import java.util.List;

public class MinGreenAdaptiveStrategy implements PhaseStrategy{

    private final List<Phase> plan;

    private int currentIndex = 0;
    private int ticksInPhase = 0;

    public MinGreenAdaptiveStrategy(List<Phase> plan){
        this.plan = plan;
    }

    @Override
    public Phase chooseNextPhase(Intersection intersection, int tick) {
        Phase current = plan.get(currentIndex);

        if(ticksInPhase < current.getGreenDuration()){
            ticksInPhase++;
            return current;
        }
        if(hasWaitingVehicles(intersection, current)) {
            ticksInPhase++;
            return current;
        }

        currentIndex = (currentIndex + 1) % plan.size();
        ticksInPhase = 1;
        return plan.get(currentIndex);
    }

    @Override
    public int getElapsedTicks() {
        return PhaseStrategy.super.getElapsedTicks();
    }

    private boolean hasWaitingVehicles(Intersection intersection, Phase phase){
        for(Road road : phase.getPermittedFlows()) {
            int waiting = intersection.getQueueLength(road);
            if(waiting > 0)

                return true;
        }
        return false;
    }
}
