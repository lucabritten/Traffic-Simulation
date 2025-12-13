package com.britten.simulation;

import com.britten.control.FixedCycleStrategy;
import com.britten.control.Phase;
import com.britten.control.PhaseController;
import com.britten.domain.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class SimulationEngineTest {

    @Test
    void engineStoresMutableVehicleList() throws Exception{

        Intersection i1 = new Intersection(1);

        SimulationEngine engine = new SimulationEngine(
                List.of(new Car(1, new Road(i1, i1, 10), 1)),
                List.of(i1),
                new MovementEngine()
        );

        Field f = SimulationEngine.class.getDeclaredField("vehicles");
        f.setAccessible(true);

        Object list = f.get(engine);

        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    void runUntilDone_endsAsExpected(){
        Intersection i1 = new Intersection(1);
        Intersection i2 = new Intersection(2);

        Road r1 = new Road(i1, i2,10);
        Road r2 = new Road(i2,i1, 10);

        i1.addOutgoingRoad(r1);
        i1.addIncomingRoad(r2, new TrafficLight(1));

        i2.addOutgoingRoad(r2);
        i2.addIncomingRoad(r1, new TrafficLight(2));

        Phase phase = new Phase(Set.of(r1,r2), 100,1,1);

        PhaseController phaseController = new PhaseController(new FixedCycleStrategy(List.of(phase)));
        i1.setController(phaseController);
        i2.setController(phaseController);

        Vehicle vehicle = new Car(1, r1,1);
        vehicle.setRoute(List.of(r1,r2));
        vehicle.enableRouting();

        SimulationEngine engine = new SimulationEngine(
                List.of(vehicle),
                List.of(i1,i2),
                new MovementEngine()
        );

        int expectedTicks = 20;

        engine.runUntilDone();
        assertThat(engine.getGlobalTick()).isEqualTo(expectedTicks);
        assertThat(vehicle.hasDestinationReached()).isTrue();


    }

}
