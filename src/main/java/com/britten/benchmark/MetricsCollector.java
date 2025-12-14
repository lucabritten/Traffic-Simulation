package com.britten.benchmark;

import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventListener;

import java.util.HashMap;
import java.util.Map;

public class MetricsCollector implements SimulationEventListener {

    private final Map<Integer, Integer> vehicleStartTick = new HashMap<>();
    private final Map<Integer, Integer> vehicleEndTick = new HashMap<>();
    private final Map<Integer, Integer> vehicleWaitingTicks = new HashMap<>();
    private final Map<Integer, Integer> vehicleStopCount = new HashMap<>();

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
                vehicleEndTick.put(vehicleId, tick);
            }
            case "VEHICLE_STOPPED" -> {
                int vehicleId = (int) event.data().get("vehicleId");
                vehicleWaitingTicks.merge(vehicleId, 1, Integer::sum);
                vehicleStopCount.merge(vehicleId, 1, Integer::sum);
            }
            case "VEHICLE_WAITING" -> {
                int vehicleId = (int) event.data().get("vehicleId");
                vehicleWaitingTicks.merge(vehicleId, 1, Integer::sum);
            }
        }
    }

    public SimulationResult buildResult(int totalTicks) {

        int arrived = vehicleEndTick.size();

        double avgTravelTime = vehicleEndTick.entrySet().stream()
                .mapToInt(e -> e.getValue() - vehicleStartTick.getOrDefault(e.getKey(), 0))
                .average()
                .orElse(0.0);

        int maxTravelTime = vehicleEndTick.entrySet().stream()
                .mapToInt(e -> e.getValue() - vehicleStartTick.getOrDefault(e.getKey(), 0))
                .max()
                .orElse(0);

        double avgWaitingTime = vehicleWaitingTicks.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        int totalStops = vehicleStopCount.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        return new SimulationResult(
                totalTicks,
                arrived,
                avgTravelTime,
                maxTravelTime,
                avgWaitingTime,
                totalStops
        );
    }
}
