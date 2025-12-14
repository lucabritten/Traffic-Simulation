package com.britten.benchmark;

import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventListener;

import java.util.HashMap;
import java.util.Map;

public class MetricsCollector implements SimulationEventListener {

    private final Map<Integer, Integer> vehicleStartTick = new HashMap<>();
    private final Map<Integer, Integer> vehiclesEndTick = new HashMap<>();

    @Override
    public void onEvent(SimulationEvent event) {
        switch (event.type()) {
            case "VEHICLE_SPAWNED" -> {
                int vehicleId = (int) event.data().get("vehicleId");
                int tick = event.tick();
                vehicleStartTick.put(vehicleId, tick);
            }
            case "VEHICLE_ARRIVED" -> {
                int vehicleId = (int) event.data().get("vehicleId");
                int tick = event.tick();
                vehiclesEndTick.put(vehicleId, tick);
            }
        }
    }

    public SimulationResult buildResult(int totalTicks){
            int arrived = vehiclesEndTick.size();

            double avgTravelTime = vehiclesEndTick.entrySet().stream()
                    .mapToInt(e -> e.getValue() - vehicleStartTick.getOrDefault(e.getKey(), 0))
                    .average()
                    .orElse(0.0);

            return new SimulationResult(totalTicks, arrived, avgTravelTime);
        }
}
