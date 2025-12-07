package com.britten.logging;

import java.util.Map;

public record SimulationEvent(
        String type,
        int tick,
        Map<String, Object> data
)
{}
