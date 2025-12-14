package com.britten.benchmark;

public class SimulationResult {

    private final int totalTicks;
    private final int vehiclesArrived;
    private final double averageTravelTime;

    public SimulationResult(int totalTicks, int vehiclesArrived, double averageTravelTime) {
        this.totalTicks = totalTicks;
        this.vehiclesArrived = vehiclesArrived;
        this.averageTravelTime = averageTravelTime;
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

    @Override
    public String toString(){
        return """
                SimulationResult
                ======================
                totalTicks            = %d
                vehiclesArrived       = %d
                averageTravelTime     = %.2f
                """.formatted(totalTicks, vehiclesArrived, averageTravelTime);
    }
}
