package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.Vehicle;

import java.util.List;
import java.util.Set;

public class MovementEngine {

    private static final int SAFE_DISTANCE = 2;
    private final IntersectionController intersectionController;

    public MovementEngine(){
        intersectionController = new IntersectionController();
    }

    public void computeNext(Vehicle vehicle, Snapshot snapshot, int distance){
        Road road = vehicle.getCurrentRoad();
        List<Vehicle> vehiclesOnRoad = snapshot.getRoadVehicles().get(road);

        int index = vehiclesOnRoad.indexOf(vehicle);
        if (index > 0) {
            Vehicle front = vehiclesOnRoad.get(index - 1);

            int projectedFrontPos = front.getPosition() + distance * front.getSpeed();
            int projectedPos = vehicle.getPosition() + distance * vehicle.getSpeed();

            if (front.getSpeed() == 0) {
                projectedFrontPos = front.getPosition();
            }

            if (projectedPos >= projectedFrontPos - SAFE_DISTANCE) {
                int targetPos = projectedFrontPos - SAFE_DISTANCE;
                if (targetPos < vehicle.getPosition()) {
                    targetPos = vehicle.getPosition();
                }

                vehicle.setNextPosition(targetPos);
                vehicle.setNextSpeed(0);
                vehicle.setNextRoad(road);
                return;
            }
        }

        int newPosition = vehicle.getPosition() + distance * vehicle.getSpeed();
        if(newPosition >= road.getLength()){
            computeNextEndOfRoad(vehicle, road ,snapshot , distance);
            return;
        }
        vehicle.setNextPosition(newPosition);
        vehicle.resetNextSpeedToDefault();
        vehicle.setNextRoad(road);
    }

    private void computeNextEndOfRoad(Vehicle vehicle, Road road, Snapshot snapshot, int distance) {
        if(!intersectionController.mayEnter(vehicle,road,snapshot)){
            vehicle.setNextRoad(road);
            vehicle.setNextPosition(road.getLength());
            vehicle.setNextSpeed(0);
            return;
        }

        Road next = vehicle.peekNextRoad();

        if(next == null) {
            vehicle.getCurrentRoad().removeVehicle(vehicle);
            vehicle.setCurrentRoad(null);
            return;
        }
        vehicle.advanceToNextRoad();

        int remainingDistance = (distance * vehicle.getDefaultSpeed()) - (road.getLength() - vehicle.getPosition());

        vehicle.setNextRoad(next);
        vehicle.setNextPosition(remainingDistance);
        vehicle.resetNextSpeedToDefault();
    }
}
