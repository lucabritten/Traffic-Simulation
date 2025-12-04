package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.domain.Vehicle;

public class MovementEngine {

    public void move(Vehicle vehicle, int delta){
        double distance = vehicle.getSpeed() * delta;
        int position = vehicle.getPosition();
        Road road = vehicle.getCurrentRoad();

        if (position + distance >= road.getLength()) {
            handleEndOfRoad(vehicle, road, distance);
        } else {
            vehicle.setPosition((int)(position + distance));
        }
    }

    private void handleEndOfRoad(Vehicle vehicle, Road road, double distance) {

        int newPos = vehicle.getPosition() + (int)distance;
        int overflow = newPos - road.getLength();

        TrafficLight.State lightState = road.getTo().getTrafficLight().getState();

        if (lightState == TrafficLight.State.RED) {
            vehicle.setPosition(road.getLength());
            return;
        }

        if (!road.getTo().getOutgoingRoads().isEmpty()) {
            vehicle.setCurrentRoad(road.getTo().getOutgoingRoads().getFirst());
            vehicle.setPosition(overflow);
        } else {
            vehicle.setPosition(road.getLength());
        }
    }
}
