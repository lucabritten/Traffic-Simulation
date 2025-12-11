package com.britten.simulation;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.domain.Vehicle;
import com.britten.logging.SimulationEvent;
import com.britten.logging.SimulationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MovementEngine {

    private static final int SAFE_DISTANCE = 2;
    private static final int VEHICLE_STANDING = 0;

    private final IntersectionController intersectionController;
    private SimulationEventPublisher publisher;

    private final Random rng = new Random();


    public MovementEngine(){
        intersectionController = new IntersectionController();
    }

    public void setPublisher(SimulationEventPublisher publisher){
        this.publisher = publisher;
    }

    public void computeNext(Vehicle vehicle, Snapshot snapshot, int distance){

        if(handleIfVehicleAhead(vehicle,snapshot,distance)) return;
        if(handelIfYellowLight(vehicle)) return;
        if(handleEndOfRoad(vehicle,snapshot,distance)) return;

        applyDefaultMovement(vehicle,distance);
    }

    /**
     * Handles collision avoidance with a vehicle in front.
     *
     * @return true if the method handled movement fully and computeNext should stop
     */
    private boolean handleIfVehicleAhead(Vehicle vehicle, Snapshot snapshot, int distance){
        Road road = vehicle.getCurrentRoad();
        List<Vehicle> vehiclesOnRoad = snapshot.getRoadVehicles().get(road);

        if (isVehicleAhead(vehiclesOnRoad, vehicle)) {
            Vehicle front = vehiclesOnRoad.get(vehiclesOnRoad.indexOf(vehicle) - 1);

            int projectedFrontPos = calcProjectedPosition(front, distance);

            if (isVehicleToClose(front, vehicle, distance)) {
                int targetPos = projectedFrontPos - SAFE_DISTANCE;
                if (targetPos < vehicle.getPosition()) {
                    targetPos = vehicle.getPosition();
                }

                vehicle.setNextPosition(targetPos);
                vehicle.setNextSpeed(VEHICLE_STANDING);
                vehicle.setNextRoad(road);
                return true;
            }
        }
        return false;
    }

    private boolean isVehicleAhead(List<Vehicle> vehiclesOnRoad, Vehicle vehicle){
        return vehiclesOnRoad.indexOf(vehicle) > 0;
    }

    private boolean isVehicleToClose(Vehicle front, Vehicle vehicle, int distance){
        int projectedFrontPos = calcProjectedPosition(front, distance);
        int projectedPos = calcProjectedPosition(vehicle, distance);

        return projectedPos >= projectedFrontPos - SAFE_DISTANCE;
    }

    /**
     * Handles behavior when approaching a YELLOW traffic light.
     *
     * @return true if movement was handled and computeNext should exit
     */
    private boolean handelIfYellowLight(Vehicle vehicle){
        Road road = vehicle.getCurrentRoad();
        var intersection = road.getTo();
        var light = intersection.getLightFor(road);

        if (light != null && light.getState() == TrafficLight.State.YELLOW) {
            int distanceToIntersection = road.getLength() - vehicle.getPosition();

            if (distanceToIntersection <= SAFE_DISTANCE + vehicle.getSpeed()) {
                vehicle.setNextRoad(road);
                vehicle.setNextPosition(road.getLength());
                vehicle.setNextSpeed(0);
                return true;
            }
        }
        return false;
    }

    /**
     * Handles movement when the vehicle approximately reaches or crosses the end of the road.
     *
     * @return true if the vehicle's next state was fully handled
     */
    private boolean handleEndOfRoad(Vehicle vehicle, Snapshot snapshot, int distance){
        Road road = vehicle.getCurrentRoad();
        int newPosition = calcProjectedPosition(vehicle,distance);
        if(newPosition >= road.getLength()){
            computeNextEndOfRoad(vehicle, road ,snapshot , distance);
            return true;
        }
        return false;
    }

    /**
     * Applies the normal forward movement for this tick.
     */
    private void applyDefaultMovement(Vehicle vehicle, int distance){
        Road road = vehicle.getCurrentRoad();
        int newPosition = calcProjectedPosition(vehicle,distance);

        vehicle.setNextPosition(newPosition);
        vehicle.resetNextSpeedToDefault();
        vehicle.setNextRoad(road);
    }

    private int calcProjectedPosition(Vehicle vehicle, int distance){
        return vehicle.getPosition() + distance * vehicle.getSpeed();
    }

    private void computeNextEndOfRoad(Vehicle vehicle, Road road, Snapshot snapshot, int distance) {

        if(handleIntersectionEntry(vehicle,road,snapshot)) return;

        Road next = determineNextRoad(vehicle, road);

        if(handleVehicleArrival(vehicle, vehicle.getCurrentRoad(), snapshot, next)) return;

        moveVehicleToNextRoad(vehicle, vehicle.getCurrentRoad(), next, distance);
    }

    /**
     * Checks if the vehicle is allowed to enter the intersection.
     *
     * @return true if a vehicle is not allowed to enter and stopped.
     */
    private boolean handleIntersectionEntry(Vehicle vehicle, Road road, Snapshot snapshot) {
        if(!intersectionController.mayEnter(vehicle,road,snapshot)){
            vehicle.setNextRoad(road);
            vehicle.setNextPosition(road.getLength());
            vehicle.setNextSpeed(0);
            return true;
        }
        return false;
    }

    /**
     * Determines the next road the vehicle should take,
     * either via routing or outgoing-road selection.
     */
    private Road determineNextRoad(Vehicle vehicle, Road currentRoad){
        if (vehicle.isRoutingEnabled())
            return vehicle.peekNextRoad();

        Set<Road> outgoingRoads = currentRoad.getTo().getOutgoingRoads();
        if (outgoingRoads == null || outgoingRoads.isEmpty())
            return null;

        return getRandomRoad(currentRoad.getTo());
    }

    private Road getRandomRoad(Intersection intersection){
        Set<Road> outgoing = intersection.getOutgoingRoads();

        List<Road> roadList = outgoing.stream().toList();

        int index = rng.nextInt(roadList.size());

        return roadList.get(index);

    }

    /**
     * Handles arrival logic when no next road exists.
     *
     * @return true if the vehicle arrived and no further processing is needed
     */
    private boolean handleVehicleArrival(
            Vehicle vehicle,
            Road currentRoad,
            Snapshot snapshot,
            Road nextRoad
    ) {
        // Case 1: routing enabled → check destination
        if (vehicle.isRoutingEnabled() && !vehicle.hasDestinationReached()) {
            if (!vehicle.getDestination().equals(vehicle.getCurrentRoad())) {
                // Destination not yet reached
                return false;
            }
            // Destination reached → arrival handled below
        }
        // Case 2: routing disabled → arrival only if dead-end
        else {
            if (nextRoad != null) {
                return false;
            }
        }

        // Arrival logic
        vehicle.setNextRoad(currentRoad);
        vehicle.setNextPosition(currentRoad.getLength());
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
                                    "roadId", currentRoad,
                                    "destinationId",
                                    vehicle.getDestination() != null
                                            ? vehicle.getDestination()
                                            : currentRoad.getTo().getId()
                            )
                    )
            );
        }
        return true;
    }

    /**
     *
     * Moves the vehicle onto the next road and computes its new position.
     */
    private void moveVehicleToNextRoad(
            Vehicle vehicle,
            Road currentRoad,
            Road nextRoad,
            int distance
    ) {
        vehicle.advanceToNextRoad();

        int remainingDistance = calcRemainingDistance(vehicle, currentRoad, distance);

        vehicle.setNextRoad(nextRoad);
        vehicle.setNextPosition(remainingDistance);
        vehicle.resetNextSpeedToDefault();
    }

    private int calcRemainingDistance(Vehicle vehicle, Road currentRoad, int distance){
        return (distance * vehicle.getDefaultSpeed()) - (currentRoad.getLength() - vehicle.getPosition());
    }
}
