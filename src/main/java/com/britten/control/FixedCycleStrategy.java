package com.britten.control;

import com.britten.domain.Intersection;

import java.util.List;

public class FixedCycleStrategy implements PhaseStrategy {

    private final List<Phase> plan;
    private int index = 0;
    private int ticksInPhase = 0;

    public FixedCycleStrategy(List<Phase> plan) {
        this.plan = plan;
    }

    @Override
    public Phase chooseNextPhase(Intersection i, int tick) {
        Phase p = plan.get(index);

        if (ticksInPhase >= p.totalDuration()) {
            index = (index + 1) % plan.size();
            ticksInPhase = 0;
            p = plan.get(index);
        }

        // advance internal counter AFTER selecting current phase
        ticksInPhase++;
        return p;
    }

    @Override
    public int getElapsedTicks() {
        return ticksInPhase;
    }
}
