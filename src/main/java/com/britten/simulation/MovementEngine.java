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

        if(handleIfVehicleAhead(vehicle,snapshot,distance)) {
            publishVehicleWaitingIfNeeded(vehicle, snapshot);
            return;
        }
        if(handelIfYellowLight(vehicle, snapshot)) {
            publishVehicleWaitingIfNeeded(vehicle, snapshot);
            return;
        }
        if(handleEndOfRoad(vehicle,snapshot,distance)) {
            publishVehicleWaitingIfNeeded(vehicle, snapshot);
            return;
        }

        applyDefaultMovement(vehicle,distance);
        publishVehicleWaitingIfNeeded(vehicle, snapshot);
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
                publishVehicleStoppedIfNeeded(vehicle, snapshot);
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
    private boolean handelIfYellowLight(Vehicle vehicle, Snapshot snapshot){
        Road road = vehicle.getCurrentRoad();
        var intersection = road.getTo();
        var light = intersection.getLightFor(road);

        if (light != null && light.getState() == TrafficLight.State.YELLOW) {
            int distanceToIntersection = road.getLength() - vehicle.getPosition();

            if (distanceToIntersection <= SAFE_DISTANCE + vehicle.getSpeed()) {
                vehicle.setNextRoad(road);
                vehicle.setNextPosition(road.getLength());
                vehicle.setNextSpeed(VEHICLE_STANDING);
                publishVehicleStoppedIfNeeded(vehicle, snapshot);
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

        if(vehicle.getRoute() != null && vehicle.peekNextRoad() == vehicle.getDestination()) {
            handleArrivalEvent(vehicle, road, snapshot);
            return;
        }

        if(handleIntersectionEntry(vehicle,road,snapshot)) return;

        Road next = determineNextRoad(vehicle, road);

        if(next == null){
            handleArrivalEvent(vehicle, road, snapshot);
            return;
        }

        moveVehicleToNextRoad(vehicle, road, next, distance);
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
            vehicle.setNextSpeed(VEHICLE_STANDING);
            publishVehicleStoppedIfNeeded(vehicle, snapshot);
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

    private void handleArrivalEvent(Vehicle vehicle, Road road, Snapshot snapshot){
        vehicle.setNextRoad(road);
        vehicle.setNextPosition(road.getLength());
        vehicle.setNextSpeed(VEHICLE_STANDING);
        publishVehicleStoppedIfNeeded(vehicle, snapshot);

        vehicle.setRoute(null);

        if(publisher != null){
            publisher.publish(
                    new SimulationEvent(
                            "VEHICLE_ARRIVED",
                            snapshot.getTick(),
                            Map.of(
                                    "vehicleId", vehicle.getId(),
                                    "intersectionId", road.getTo().getId()
                            )
                    )
            );
        }
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

    // Helper to publish VEHICLE_STOPPED event if speed transitions from >0 to 0
    private void publishVehicleStoppedIfNeeded(Vehicle vehicle, Snapshot snapshot) {
        if (publisher == null || snapshot == null) return;

        if (vehicle.getSpeed() > 0 && vehicle.getNextSpeed() == 0) {
            publisher.publish(
                    new SimulationEvent(
                            "VEHICLE_STOPPED",
                            snapshot.getTick(),
                            Map.of("vehicleId", vehicle.getId())
                    )
            );
        }
    }

    // Helper to publish VEHICLE_WAITING event if speed == 0 and nextSpeed == 0
    private void publishVehicleWaitingIfNeeded(Vehicle vehicle, Snapshot snapshot) {
        if (publisher == null || snapshot == null) return;

        if (vehicle.getSpeed() == 0 && vehicle.getNextSpeed() == 0) {
            publisher.publish(
                    new SimulationEvent(
                            "VEHICLE_WAITING",
                            snapshot.getTick(),
                            Map.of("vehicleId", vehicle.getId())
                    )
            );
        }
    }
}
