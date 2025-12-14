package com.britten.testutil;

import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class TestEventPublisher implements SimulationEventPublisher {

    private final List<SimulationEvent> events = new ArrayList<>();

    @Override
    public void publish(SimulationEvent event) {
        events.add(event);
    }

    public List<SimulationEvent> events(){
        return events;
    }

    public List<SimulationEvent> eventsOfType(String type) {
        return events.stream()
                .filter(e -> e.type().equals(type))
                .toList();
    }
}
