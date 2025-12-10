package com.britten.control;

import com.britten.domain.Intersection;

import java.util.List;

public class FixedCycleStrategy implements PhaseStrategy {
    private final List<Phase> signalPlan;
    private int activePhaseIndex = 0;
    private int ticksInPhase = 0;

    public FixedCycleStrategy(List<Phase> signalPlan) {
        this.signalPlan = signalPlan;
    }

    @Override
    public Phase chooseNextPhase(Intersection intersection, int tick) {
        Phase current = signalPlan.get(activePhaseIndex);

        if (ticksInPhase >= current.totalDuration()) {
            activePhaseIndex = (activePhaseIndex + 1) % signalPlan.size();
            ticksInPhase = 0;
        }

        ticksInPhase++;
        return current;
    }
}
