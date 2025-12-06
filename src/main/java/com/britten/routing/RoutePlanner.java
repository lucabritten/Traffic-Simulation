package com.britten.routing;

import com.britten.domain.Intersection;
import com.britten.domain.Road;

import java.util.List;

public interface RoutePlanner {

    /**
     * Computes the optimal route from a start intersection to a goal intersection.
     *
     * @param network the full road network graph
     * @param start the starting intersection
     * @param goal the target intersection
     * @return a list of roads representing the route (may be empty if unreachable)
     */
    List<Road> computeRoute(RoadNetwork network, Intersection start, Intersection goal);
}
