package com.britten.control;

import com.britten.domain.Intersection;

public interface PhaseStrategy {
    Phase chooseNextPhase(Intersection intersection, int tick);
}
