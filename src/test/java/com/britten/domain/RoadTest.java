package com.britten.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RoadTest {

    @Test
    void testInitializeRoadWithValidArgs(){
        Intersection from = new Intersection(1,new TrafficLight(1,1,1));
        Intersection to = new Intersection(2,new TrafficLight(1,1,1));
        int length = 100;

        Road road = new Road(from,to,length);

        assertThat(road).isNotNull();

        assertThat(road.getFrom()).isEqualTo(from);
        assertThat(road.getTo()).isEqualTo(to);
        assertThat(road.getLength()).isEqualTo(length);
    }

    @Test
    void testInitializeRoadWithNegativeLengthThrowsException(){
        int length = -1;
        Intersection from = new Intersection(1,new TrafficLight(1,1,1));
        Intersection to = new Intersection(2,new TrafficLight(1,1,1));

        assertThatThrownBy(() -> new Road(from, to,length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length cannot be negative or zero");
    }

    @Test
    void testInitializeRoadWithZeroLengthThrowsException(){
        int length = 0;
        Intersection from = new Intersection(1,new TrafficLight(1,1,1));
        Intersection to = new Intersection(2,new TrafficLight(1,1,1));

        assertThatThrownBy(() -> new Road(from, to,length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length cannot be negative or zero");
    }

    @Test
    void testInitializeRoadWithFromNullThrowsException(){
        int length = 10;
        Intersection from = null;
        Intersection to = new Intersection(2,new TrafficLight(1,1,1));

        assertThatThrownBy(() -> new Road(from, to,length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Intersection from and to are not allowed to be null!");
    }

    @Test
    void testInitializeRoadWithToNullThrowsException(){
        int length = 10;
        Intersection from = new Intersection(2,new TrafficLight(1,1,1));
        Intersection to = null;

        assertThatThrownBy(() -> new Road(from, to,length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Intersection from and to are not allowed to be null!");
    }
}
