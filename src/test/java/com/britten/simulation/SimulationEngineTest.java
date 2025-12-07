package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulationEngineTest {

    @Test
    void engineStoresMutableVehicleList() throws Exception{

        Intersection i1 = new Intersection(1, new TrafficLight(new FixedCycleStrategy(3,1,5)));

        SimulationEngine engine = new SimulationEngine(
                List.of(new Car(1, new Road(i1, i1, 10), 1)),
                List.of(i1),
                new MovementEngine()
        );

        Field f = SimulationEngine.class.getDeclaredField("vehicles");
        f.setAccessible(true);

        Object list = f.get(engine);

        assertTrue(list instanceof ArrayList);
    }

}
