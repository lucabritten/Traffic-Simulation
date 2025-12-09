package com.britten.routing;

import com.britten.control.FixedCycleStrategy;
import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.TrafficLight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DijkstraRoutePlannerTest {

    private DijkstraRoutePlanner planner;
    private RoadNetwork network;

    private Intersection i1, i2, i3, i4, i5, i6, i99;
    private Road r12, r14, r23, r26, r34, r35, r45;

    @BeforeEach
    void setUp(){
        planner = new DijkstraRoutePlanner();

        i1 = new Intersection(1);
        i2 = new Intersection(2);
        i3 = new Intersection(3);
        i4 = new Intersection(4);
        i5 = new Intersection(5);
        i6 = new Intersection(6);
        i99 = new Intersection(99); //unreachable intersection

        r12 = new Road(i1, i2, 20);
        r14 = new Road(i1, i4, 80);
        r23 = new Road(i2, i3, 25);
        r26 = new Road(i2, i6, 20);
        r34 = new Road(i3, i4, 15);
        r35 = new Road(i3, i5, 30);
        r45 = new Road(i4, i5, 10);

        i1.addOutgoingRoads(List.of(r12, r14));
        i2.addOutgoingRoads(List.of(r23, r26));
        i3.addOutgoingRoads(List.of(r34, r35));
        i4.addOutgoingRoads(List.of(r45));
        i5.addOutgoingRoads(List.of());

        network = new RoadNetwork(List.of(i1,i2,i3,i4,i5,i6,i99),
                List.of(r12, r14, r23, r26, r34, r35, r45)
        );
    }

    @Test
    void findDirectNeighborRoute() {
        List<Road> route = planner.computeRoute(network, i1, i2);

        List<Road> expectedRoute = List.of(r12);

        assertThat(route).isNotNull();
        assertThat(route).isNotEmpty();
        assertThat(route).isEqualTo(expectedRoute);
    }

    @Test
    void findMultiHopRoute() {
        List<Road> route = planner.computeRoute(network, i1, i6);
        List<Road> expectedRoute = List.of(r12,r26);

        int length = route.stream()
                        .map(Road::getLength)
                        .reduce(0, Integer::sum);
        int expectedLength = 40;

        assertThat(route).isEqualTo(expectedRoute);
        assertThat(length).isEqualTo(expectedLength);
    }

    @Test
    void findShortestAmongMultiplePaths() {
        List<Road> route = planner.computeRoute(network, i1, i5);
        List<Road> expectedRoute = List.of(r12, r23, r34, r45);

        assertThat(route).isEqualTo(expectedRoute);
    }

    @Test
    void unreachableNodeReturnsEmptyRoute() {
        List<Road> route = planner.computeRoute(network, i1, i99);

        assertThat(route).isNotNull();
        assertThat(route).isEmpty();
    }

    @Test
    void startEqualsGoalReturnsEmptyRoute() {
        List<Road> route = planner.computeRoute(network, i1, i1);

        assertThat(route).isNotNull();
        assertThat(route).isEmpty();
    }

    @Test
    void routingUsesRoadLengthAsWeight() {
        List<Road> route = planner.computeRoute(network, i1, i5);
        List<Road> expectedRoute = List.of(r12, r23, r34, r45);

        int length = route.stream()
                .map(Road::getLength)
                .reduce(0, Integer::sum);
        int expectedLength = 70;

        assertThat(route).isEqualTo(expectedRoute);
        assertThat(length).isEqualTo(expectedLength);
    }

    @Test
    void dijkstraIsDeterministicForSameGraph() {
        List<Road> first = planner.computeRoute(network, i1, i5);
        List<Road> second = planner.computeRoute(network, i1, i5);
        List<Road> third = planner.computeRoute(network, i1, i5);

        List<Road> expectedRoute = List.of(r12, r23, r34, r45);

        assertThat(first)
                .isEqualTo(second)
                .isEqualTo(third)
                .isEqualTo(expectedRoute);
    }

    @Test
    void choosesCorrectOutgoingRoadsOnDirectedEdges() {
        List<Road> route = planner.computeRoute(network, i2, i1); //Road i1 -> i2 exists, but i2 -> i1 not

        assertThat(route).isNotNull();
        assertThat(route).isEmpty();
    }

    @Test
    void routeReconstructionBuildsCorrectSequence() {
        List<Road> route = planner.computeRoute(network, i1, i4);

        // i1 → i2 → i3 → i4
        List<Road> expected = List.of(r12, r23, r34);

        assertThat(route).isEqualTo(expected);
    }
}
