package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.Vehicle;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovementEngine {

    private static final int SAFE_DISTANCE = 2;
    private final IntersectionController intersectionController;
    private SimulationEventPublisher publisher;

    public MovementEngine(){
        intersectionController = new IntersectionController();
    }

    public void setPublisher(SimulationEventPublisher publisher){
        this.publisher = publisher;
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
        var intersection = road.getTo();
        var light = intersection.getLightFor(road);
        if (light != null && "YELLOW".equals(light.getState().name())) {
            int distanceToIntersection = road.getLength() - vehicle.getPosition();
            // if too close to comfortably cross, stop at safe distance
            if (distanceToIntersection <= SAFE_DISTANCE + vehicle.getSpeed()) {
                vehicle.setNextRoad(road);
                vehicle.setNextPosition(road.getLength() - SAFE_DISTANCE);
                vehicle.setNextSpeed(0);
                return;
            }
        }
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

        Road next = null;

        if (vehicle.isRoutingEnabled()) {
            next = vehicle.peekNextRoad();
        } else {
            Set<Road> outgoing = road.getTo().getOutgoingRoads();
            if (outgoing == null || outgoing.isEmpty()) {
                next = null;
            } else {
                next = outgoing.iterator().next();
            }
        }

        if (next == null || (vehicle.isRoutingEnabled() && vehicle.getDestination().equals(vehicle.getCurrentRoad()))) {
            vehicle.setNextRoad(road);
            vehicle.setNextPosition(road.getLength());
            vehicle.setNextSpeed(0);

            vehicle.setRoute(null);
            vehicle.advanceToNextRoad();

            if (publisher != null) {
                publisher.publish(
                        new SimulationEvent(
                                "VEHICLE_ARRIVED",
                                snapshot.getTick(),
                                Map.of(
                                        "vehicleId", vehicle.getId(),
                                        "roadId", road,
                                        "destinationId", road.getTo().getId()
                                )
                        )
                );
            }
            return;
        }
        vehicle.advanceToNextRoad();

        int remainingDistance = (distance * vehicle.getDefaultSpeed()) - (road.getLength() - vehicle.getPosition());

        vehicle.setNextRoad(next);
        vehicle.setNextPosition(remainingDistance);
        vehicle.resetNextSpeedToDefault();
    }
}
