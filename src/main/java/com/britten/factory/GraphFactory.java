package com.britten.factory;

import com.britten.domain.Intersection;
import com.britten.domain.Vehicle;
import com.britten.routing.RoadNetwork;

import java.util.List;

public interface GraphFactory {

    RoadNetwork buildNetwork();
    List<Vehicle> createVehicles(RoadNetwork network);
    List<Intersection> getIntersections();
}
