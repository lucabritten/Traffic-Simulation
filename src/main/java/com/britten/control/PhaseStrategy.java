package com.britten.control;

import com.britten.domain.Intersection;

public interface PhaseStrategy {
    /**
     * Returns the Phase that should be active for the given intersection at the current tick.
     */
    Phase chooseNextPhase(Intersection intersection, int tick);

    /**
     * Optional: returns how many ticks the strategy has already spent in the current phase.
     * Default implementation returns 0; strategies that keep internal tick counters should override this.
     */
    default int getElapsedTicks() { return 0; }
}
