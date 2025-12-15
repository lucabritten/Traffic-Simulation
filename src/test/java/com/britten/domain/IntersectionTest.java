package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IntersectionTest {

    @Test
    void testInitializeObjectWithValidArgs(){
        int id = 1;

        Intersection intersection = new Intersection(id);
        assertThat(intersection).isNotNull();

        assertThat(intersection.getId()).isEqualTo(id);
        assertThat(intersection.getOutgoingRoads()).isNotNull();
    }

    @Test
    void testAddRoadWithValidArg(){
        int id1 = 1;
        Intersection intersection1 = new Intersection(id1);

        int id2 = 2;
        Intersection intersection2 = new Intersection(id2);

        Road road = new Road(intersection1,intersection2,100);

        intersection1.addOutgoingRoad(road);

        assertThat(intersection1.getOutgoingRoads().size()).isEqualTo(1);
        assertThat(intersection1.getOutgoingRoads().contains(road)).isTrue();
    }

    @Test
    void testAddRoadWithNullRoad(){
        int id1 = 1;
        Intersection intersection1 = new Intersection(id1);

        assertThatThrownBy(() -> intersection1.addOutgoingRoad(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Road cannot be null!");
    }

    @Test
    void testAddDuplicateRoadThrowsException(){
        int id1 = 1;
        Intersection intersection1 = new Intersection(id1);

        int id2 = 2;
        Intersection intersection2 = new Intersection(id2);

        Road road1 = new Road(intersection1,intersection2,100);
        Road road2 = new Road(intersection1,intersection2,100);

        intersection1.addOutgoingRoad(road1);

        assertThatThrownBy(() -> intersection1.addOutgoingRoad(road2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Road already exists as outgoing road at this intersection!");
    }

    @Test
    void testAddRoadThatDoesNotStartAddGivenIntersection(){
        int id1 = 1;
        Intersection intersection1 = new Intersection(id1);

        int id2 = 2;
        Intersection intersection2 = new Intersection(id2);

        Road road = new Road(intersection2,intersection1,100);

        assertThatThrownBy(() -> intersection1.addOutgoingRoad(road))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Road needs to be outgoing from this intersection!");
    }

    @Test
    void getWaitingVehicles_countsVehicle_whenWaitingAtTheEndOfRoad(){
        Intersection intersection1 = new Intersection(1);
        Intersection intersection2 = new Intersection(2);
        Road road = new Road(intersection1, intersection2, 100);
        TrafficLight light = new TrafficLight(1);
        Vehicle car = new Car(1, road, 10);

        car.setPosition(100);
        car.setSpeed(0);
        road.addVehicle(car);
        intersection2.addIncomingRoad(road,light);

        assertThat(intersection2.getVehiclesWaitingAtStopline(road))
                .isEqualTo(1);
    }

    @Test
    void getQueueLength_countsAllWaitingVehicles(){
        Intersection intersection1 = new Intersection(1);
        Intersection intersection2 = new Intersection(2);
        Road road = new Road(intersection1, intersection2, 100);
        TrafficLight light = new TrafficLight(1);

        Vehicle car1 = new Car(1, road, 10);
        Vehicle car2 = new Car(2, road, 10);
        Vehicle car3 = new Car(3, road, 10);

        car1.setPosition(100);
        car1.setSpeed(0);
        road.addVehicle(car1);

        car2.setPosition(98);
        car2.setSpeed(0);
        road.addVehicle(car2);

        car3.setPosition(96);
        car3.setSpeed(0);
        road.addVehicle(car3);
        intersection2.addIncomingRoad(road,light);

        assertThat(intersection2.getQueueLength(road))
                .isEqualTo(3);
    }
}
