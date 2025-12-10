package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.Vehicle;

public class IntersectionController {

    public boolean mayEnter(Vehicle vehicle, Road from, Snapshot snapshot){
        String state = snapshot.getLightStates().get(from);
        if (state == null) return false;

        // RED → always stop
        if ("RED".equals(state)) return false;

        // YELLOW → allow only if vehicle is already moving and not stopped at the intersection
        if ("YELLOW".equals(state)) {
            var list = snapshot.getRoadVehicles().get(from);
            if (list == null || list.isEmpty()) return false;
            // vehicle must be first in queue
            if (!list.getFirst().equals(vehicle)) return false;
            // vehicle must have speed > 0 (already crossing)
            return vehicle.getSpeed() > 0;
        }

        // GREEN → only the front vehicle may enter
        if ("GREEN".equals(state)) {
            var list = snapshot.getRoadVehicles().get(from);
            if (list == null || list.isEmpty()) return true;
            return list.getFirst().equals(vehicle);
        }

        return false;
    }
}
