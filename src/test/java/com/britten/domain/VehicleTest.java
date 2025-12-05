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
    }

    @Test
    void testInitializeVehicleWithNullRoadThrowsException(){
        int id = 1;
        Road road = null;

        assertThatThrownBy(() -> new Car(id,road))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current road cannot be null!");
    }
}
