package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VehicleTest {

    @Test
    void testInitializeVehicleWithValidArgs(){
        int id = 1;
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
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
    void testSetPosition_validValue_updatePosition(){
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
                100);
        Vehicle vehicle = new Car(1, road);

        vehicle.setPosition(5);

        assertThat(vehicle.getPosition()).isEqualTo(5);
    }

    @Test
    void testSetPosition_smallerValue_throwException(){
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
                100);
        Vehicle vehicle = new Car(1, road);

        vehicle.setPosition(10);

        assertThatThrownBy(() -> vehicle.setPosition(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A vehicle is not allowed to drive backwards!");
    }

    @Test
    void testSetPosition_valueGreaterRoadLength_throwException(){
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
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
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
                100);
        Vehicle vehicle = new Car(1, road1);

        Road road2 = new Road(
                new Intersection(3, new TrafficLight(1,1,1)),
                new Intersection(4,new TrafficLight(1,1,1)),
                100);

        vehicle.setCurrentRoad(road2);

        assertThat(vehicle.getCurrentRoad()).isEqualTo(road2);
    }

    @Test
    void testSetCurrentRoad_nullRoad_throwException(){
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setCurrentRoad(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Road cannot be null.");
    }

    @Test
    void testSetSpeed_negativeValue_throwException(){
        Road road = new Road(
                new Intersection(1, new TrafficLight(1,1,1)),
                new Intersection(2,new TrafficLight(1,1,1)),
                100);
        Vehicle vehicle = new Car(1, road);

        assertThatThrownBy(() -> vehicle.setSpeed(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Speed cannot be negative.");
    }
}


