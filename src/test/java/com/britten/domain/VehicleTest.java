package com.britten.domain;

import com.britten.control.FixedCycleStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class VehicleTest {

    @Test
    void testInitializeVehicleWithValidArgs(){
        int id = 1;
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        final int CAR_SPEED = 5;

        Vehicle vehicle = new Car(id,road);

        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getId()).isEqualTo(id);
        assertThat(vehicle.getSpeed()).isEqualTo(CAR_SPEED);
        assertThat(vehicle.getCurrentRoad()).isEqualTo(road);
        assertThat(vehicle.getPosition()).isZero();
    }

    @Test
    void testInitializeVehicleWithNullRoadThrowsException(){
        int id = 1;
        Road road = null;

        assertThatThrownBy(() -> new Car(id,road))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current road cannot be null!");
    }

    @Test
    void testInitializeVehicle_negativeSpeed_throwsException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100
        );

        assertThatThrownBy(() -> new Car(1, road,-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Speed and default speed cannot be negative or zero!");
    }

    @Test
    void testSetPosition_validValue_updatePosition(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100)
                ;
        Vehicle vehicle = new Car(1, road);

        vehicle.setPosition(5);

        assertThat(vehicle.getPosition()).isEqualTo(5);
    }

    @Test
    void testSetPosition_valueGreaterRoadLength_throwException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        int position = vehicle.getCurrentRoad().getLength() + 1;

        assertThatThrownBy(() -> vehicle.setPosition(position))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Value position is greater than length of current road.");
    }

    @Test
    void testSetCurrentRoad_validRoad_accept(){
        Road road1 = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road1);

        Road road2 = new Road(
                new Intersection(3),
                new Intersection(4),
                100);

        vehicle.setCurrentRoad(road2);

        assertThat(vehicle.getCurrentRoad()).isEqualTo(road2);
    }

    @Test
    void testSetCurrentRoad_nullRoad_throwException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setCurrentRoad(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Road cannot be null.");
    }

    @Test
    void testSetSpeed_negativeValue_throwException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setSpeed(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Speed cannot be negative.");
    }

    @Test
    void testSetNextPosition_negativeValue_throwsException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setNextPosition(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Next position cannot be negative or greater than next road length.");
    }

    @Test
    void testSetNextSpeed_negativeValue_throwsException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setNextSpeed(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Next speed cannot be negative.");
    }

    @Test
    void testEnableRouting_noRouteSet_throwsException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(vehicle::enableRouting)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot enable routing without a route set.");
    }

    @Test
    void testEnableRouting_emptyRoute_throwsException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        vehicle.setRoute(List.of());

        assertThatThrownBy(vehicle::enableRouting)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot enable routing without a route set.");
    }

    @Test
    void testEnableRouting_validRoute_throwsNoException(){
        Road road = new Road(
                new Intersection(1),
                new Intersection(2),
                100);
        Vehicle vehicle = new Car(1, road);

        vehicle.setRoute(List.of(road));

        assertThatNoException().isThrownBy(vehicle::enableRouting);
    }
}



