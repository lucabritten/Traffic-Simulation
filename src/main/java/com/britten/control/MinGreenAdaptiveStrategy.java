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

        boolean otherQueuesExist = hasWaitingVehiclesOnOtherPhases(intersection, current);

        // After MinGreen: if there is still demand on current phase AND
        // max green is not exceeded, we may extend the phase
        if (hasWaitingVehicles(intersection, current)
                && ticksInPhase < current.getMaxGreenDuration()) {
            ticksInPhase++;
            return current;
        }

        // If max green is reached AND other roads have demand, we must switch
        if (ticksInPhase >= current.getMaxGreenDuration()
                && otherQueuesExist) {
            currentIndex = (currentIndex + 1) % plan.size();
            ticksInPhase = 1;
            return plan.get(currentIndex);
        }

        // Otherwise: no demand elsewhere → allow staying green
        if (!otherQueuesExist) {
            ticksInPhase++;
            return current;
        }

        // Default: switch phase
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

    private boolean hasWaitingVehiclesOnOtherPhases(
            Intersection intersection,
            Phase currentPhase
    ) {
        return intersection.getIncomingRoads().stream()
                .filter(road -> !currentPhase.getPermittedFlows().contains(road))
                .anyMatch(road -> intersection.getQueueLength(road) > 0);
    }
}
