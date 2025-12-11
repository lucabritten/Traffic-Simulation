package com.britten.domain;

import com.britten.domain.*;
import com.britten.simulation.Snapshot;

import java.util.*;

/**
 * Helper for building compact synthetic Snapshots for tests.
 */
public class SnapshotBuilder {

    private final Map<Road, List<Vehicle>> roadMap = new HashMap<>();
    private final Map<Road, String> lightMap = new HashMap<>();

    private Vehicle singleVehicle;
    private Road singleRoad;
    private List<Vehicle> singleVehicles = null;

    // --- Convenience constructors ---

    public static Snapshot of(Vehicle v, Road r) {
        Map<Road, List<Vehicle>> rm = new HashMap<>();
        rm.put(r, List.of(v));

        Map<Road, String> lm = new HashMap<>();
        lm.put(r, "GREEN"); // neutral default

        return new Snapshot(rm, lm);
    }

    public static Snapshot of(Vehicle[] vehicles, Road r) {
        Map<Road, List<Vehicle>> rm = new HashMap<>();
        rm.put(r, List.of(vehicles));

        Map<Road, String> lm = new HashMap<>();
        lm.put(r, "GREEN");

        return new Snapshot(rm, lm);
    }

    public SnapshotBuilder withVehicle(Vehicle v) {
        if (this.singleVehicles == null) this.singleVehicles = new ArrayList<>();
        this.singleVehicles.add(v);
        return this;
    }

    public SnapshotBuilder withVehicles(Vehicle... vehicles) {
        if (this.singleVehicles == null) this.singleVehicles = new ArrayList<>();
        Collections.addAll(this.singleVehicles, vehicles);
        return this;
    }

    public SnapshotBuilder onRoad(Road r) {
        this.singleRoad = r;
        return this;
    }

    public Snapshot build() {
        if (singleVehicles != null && singleRoad != null) {
            roadMap.put(singleRoad, List.copyOf(singleVehicles));
        } else if (singleVehicle != null && singleRoad != null) {
            roadMap.put(singleRoad, List.of(singleVehicle));
        }
        // ensure every road present in roadMap has a default light state
        for (Road r : roadMap.keySet()) {
            lightMap.putIfAbsent(r, "GREEN");
        }
        // also ensure any explicitly added light entries are preserved
        return new Snapshot(roadMap, lightMap);
    }

    // -------------------------------------------------------------
    //      PRE-MADE SNAPSHOT CONFIGURATIONS FOR TESTS
    // -------------------------------------------------------------

    /**
     * Creates a snapshot with exactly one vehicle on one road
     * and the given light state.
     */
    public static Snapshot singleRoadSnapshot(String lightState) {
        Intersection a = new Intersection(1);
        Intersection b = new Intersection(2);
        Road r = new Road(a, b, 100);

        Vehicle v = new Car(1, r, 10);
        v.setPosition(50);

        Map<Road, List<Vehicle>> rm = new HashMap<>();
        rm.put(r, List.of(v));

        Map<Road, String> lm = new HashMap<>();
        lm.put(r, lightState);

        return new Snapshot(rm, lm);
    }

    /**
     * Two vehicles on one road: v1 in front, v2 behind.
     */
    public static Snapshot twoVehicleSnapshot(String lightState) {
        Intersection a = new Intersection(1);
        Intersection b = new Intersection(2);
        Road r = new Road(a, b, 100);

        Vehicle v1 = new Car(1, r, 10);
        Vehicle v2 = new Car(1, r, 10);

        v1.setPosition(90);
        v2.setPosition(60);

        Map<Road, List<Vehicle>> rm = new HashMap<>();
        rm.put(r, List.of(v1, v2));

        Map<Road, String> lm = new HashMap<>();
        lm.put(r, lightState);

        return new Snapshot(rm, lm);
    }

    /**
     * Front vehicle is moving → should be allowed through yellow.
     */
    public static Snapshot movingFrontVehicle(String lightState) {
        Intersection a = new Intersection(1);
        Intersection b = new Intersection(2);
        Road r = new Road(a, b, 100);

        Vehicle v = new Car(1, r, 10);
        v.setPosition(95);
        v.setSpeed(10);

        Map<Road, List<Vehicle>> rm = Map.of(r, List.of(v));
        Map<Road, String> lm = Map.of(r, lightState);

        return new Snapshot(rm, lm);
    }

    /**
     * Front vehicle stands still → must NOT enter on yellow.
     */
    public static Snapshot stoppedFrontVehicle(String lightState) {
        Intersection a = new Intersection(1);
        Intersection b = new Intersection(2);
        Road r = new Road(a, b, 100);

        Vehicle v = new Car(1, r, 10);
        v.setPosition(95);
        v.setSpeed(0);

        Map<Road, List<Vehicle>> rm = Map.of(r, List.of(v));
        Map<Road, String> lm = Map.of(r, lightState);

        return new Snapshot(rm, lm);
    }
}
