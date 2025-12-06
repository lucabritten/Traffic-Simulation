package com.britten.routing;

import com.britten.domain.Intersection;
import com.britten.domain.Road;

import java.util.List;

public class RoadNetwork {

    private final List<Intersection> intersections;
    private final List<Road> roads;

    public RoadNetwork(List<Intersection> intersections, List<Road> roads){
        this.intersections = intersections;
        this.roads = roads;
    }

    public List<Intersection> getIntersections(){
        return intersections;
    }

    public List<Road> getRoads(){
        return roads;
    }

    public List<Road> getOutgoingRoads(Intersection i){
        return roads.stream()
                .filter(r -> r.getFrom().equals(i))
                .toList();
    }
}
