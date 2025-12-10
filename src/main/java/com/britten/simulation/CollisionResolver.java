package com.britten.simulation;

import com.britten.domain.Road;
import com.britten.domain.Vehicle;

import java.util.List;
import java.util.Map;

public class CollisionResolver {

    private static final int SAFE_DISTANCE = 2;

    public void resolve(Snapshot snapshot, List<Vehicle> vehicles){
        Map<Road, List<Vehicle>> roads = snapshot.getRoadVehicles();

        for(Map.Entry<Road, List<Vehicle>> entry : roads.entrySet()){
            Road road = entry.getKey();
            List<Vehicle> ordered = entry.getValue();

            for(int i = 0; i < ordered.size() - 1; i++){
                Vehicle front = ordered.get(i);
                Vehicle back = ordered.get(i + 1);


                int frontNext = front.getNextPosition();
                int backNext = back.getNextPosition();

                if(backNext >= frontNext - SAFE_DISTANCE)
                    if(frontNext - SAFE_DISTANCE >= 0)
                    back.setNextPosition(frontNext - SAFE_DISTANCE);
                    else
                        back.setNextPosition(back.getPosition());
            }
        }
    }
}
