package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.Vehicle;

public class IntersectionController {

    public boolean mayEnter(Vehicle vehicle, Road from, Snapshot snapshot){
        String light = snapshot.getTrafficLightStates().get(from);
        if(!"GREEN".equals(light)){
            return false; //light red/yellow
        }

        var list = snapshot.getRoadVehicles().get(from);
        if(list == null || list.isEmpty())
            return true;

        return list.getFirst().equals(vehicle);
    }
}
