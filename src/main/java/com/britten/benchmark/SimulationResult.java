package com.britten.benchmark;

public class SimulationResult {

    private final int totalTicks;
    private final int vehiclesArrived;
    private final double averageTravelTime;
    private final int maxTravelTime;
    private final double averageWaitingTime;
    private final int totalStops;

    public SimulationResult(
            int totalTicks,
            int vehiclesArrived,
            double averageTravelTime,
            int maxTravelTime,
            double averageWaitingTime,
            int totalStops
    ) {
        this.totalTicks = totalTicks;
        this.vehiclesArrived = vehiclesArrived;
        this.averageTravelTime = averageTravelTime;
        this.maxTravelTime = maxTravelTime;
        this.averageWaitingTime = averageWaitingTime;
        this.totalStops = totalStops;
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    public int getVehiclesArrived() {
        return vehiclesArrived;
    }

    public double getAverageTravelTime() {
        return averageTravelTime;
    }

    public int getMaxTravelTime() { return maxTravelTime; }
    public double getAverageWaitingTime() { return averageWaitingTime; }
    public int getTotalStops() { return totalStops; }

    @Override
    public String toString() {
        return """
                SimulationResult
                ======================
                totalTicks            = %d
                vehiclesArrived       = %d
                averageTravelTime     = %.2f
                maxTravelTime         = %d
                averageWaitingTime    = %.2f
                totalStops            = %d
                """.formatted(
                    totalTicks,
                    vehiclesArrived,
                    averageTravelTime,
                    maxTravelTime,
                    averageWaitingTime,
                    totalStops
                );
    }
}
