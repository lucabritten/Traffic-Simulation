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
}
