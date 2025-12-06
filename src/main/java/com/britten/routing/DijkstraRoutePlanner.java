package com.britten.routing;

import com.britten.domain.Intersection;
import com.britten.domain.Road;

import java.util.*;

public class DijkstraRoutePlanner implements RoutePlanner{

    private Map<Intersection, Integer> distance;
    private Map<Intersection, Road> previousRoad;
    private PriorityQueue<Intersection> priorityQueue;

    public DijkstraRoutePlanner(){
        distance = new HashMap<>();
        previousRoad = new HashMap<>();
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
    }

    @Override
    public List<Road> computeRoute(RoadNetwork network, Intersection start, Intersection goal) {

        distance.clear();
        previousRoad.clear();
        priorityQueue.clear();

        //Initialize network
        for(Intersection i : network.getIntersections()){
            distance.put(i, Integer.MAX_VALUE);
            previousRoad.put(i, null);
        }

        distance.put(start, 0);
        priorityQueue.add(start);

        while(!priorityQueue.isEmpty()){
            Intersection current = priorityQueue.poll();

            if(current.equals(goal))
                break; //Destination found!

            for (Road r : current.getOutgoingRoads()){
                Intersection neighbor = r.getTo();
                int newDistance = distance.get(current) + r.getLength();
                if(newDistance < distance.get(neighbor)){
                    distance.put(neighbor, newDistance);
                    previousRoad.put(neighbor, r);
                    priorityQueue.add(neighbor);
                }
            }
        }

        List<Road> route = new ArrayList<>();

        Intersection step = goal;
        while(previousRoad.get(step) != null){
            Road r = previousRoad.get(step);
            route.add(r);
            step = r.getFrom();
        }

        Collections.reverse(route);
        return route;
    }
}
