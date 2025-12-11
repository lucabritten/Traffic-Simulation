package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import com.britten.domain.Vehicle;

public class IntersectionController {

    public boolean mayEnter(Vehicle vehicle, Road from, Snapshot snapshot){
        String string = snapshot.getLightStates().get(from);
        TrafficLight.State state = TrafficLight.State.valueOf(string);
        // RED → always stop
        if (state == TrafficLight.State.RED) return false;

        // YELLOW → allow only if vehicle is already moving and not stopped at the intersection
        if (state == TrafficLight.State.YELLOW) {
            var list = snapshot.getRoadVehicles().get(from);
            if (list == null || list.isEmpty()) return false;
            // vehicle must be first in queue
            if (!list.getFirst().equals(vehicle)) return false;
            // vehicle must have speed > 0 (already crossing)
            return vehicle.getSpeed() > 0;
        }

        // GREEN → only the front vehicle may enter
        if (state == TrafficLight.State.GREEN) {
            var list = snapshot.getRoadVehicles().get(from);
            if (list == null || list.isEmpty()) return true;
            return list.getFirst().equals(vehicle);
        }

        return false;
    }
}
