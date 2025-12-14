# Traffic Simulation Engine 🚦🚗

A modular, event-driven traffic simulation engine written in Java.  
The project models vehicles moving through a road network with intersections, traffic lights, and pluggable control strategies.  
It focuses on **correctness**, **testability**, and **extensibility**, and is designed to support **metrics-based evaluation of traffic light strategies**.

---

## ✨ Features

- Road network with intersections and directed roads
- Vehicles with speed, position, and optional routes
- Modular traffic light system with phases (GREEN / YELLOW / RED)
- Pluggable phase strategies (e.g. fixed cycle)
- Event-driven architecture (movement, waiting, arrival, etc.)
- Metrics collection based on emitted events
- Deterministic simulation via discrete ticks
- Extensive unit and integration test coverage
- CI-ready (Maven, JaCoCo)

---

## 🧠 Core Concepts

### Discrete Simulation
The simulation advances in **ticks**.  
In each tick:
1. Traffic light phases are updated
2. Vehicle movement is computed
3. Events are emitted
4. Metrics are collected

---

## 🗺️ Domain Model

### Road
Represents a directed road segment.

- start intersection
- end intersection
- length

### Intersection
Represents a node in the road graph.

- incoming roads
- outgoing roads
- a `PhaseController` that determines which incoming roads may enter

Each **incoming road has its own traffic light state**, derived from the active phase.

---

### Vehicle
Represents a moving entity in the simulation.

- current road
- position on road
- speed
- optional route (list of roads)

A vehicle:
- drives forward if possible
- stops at red/yellow lights or due to other vehicles
- emits events when stopping, waiting, or arriving

---

## 🚦 Traffic Light System

### Phase
A phase defines which incoming roads are allowed to pass and for how long.

- allowed roads (green)
- green duration
- yellow duration
- red duration

### PhaseStrategy
Strategy interface that defines how phases change over time.

Examples:
- `FixedCycleStrategy`
- (future) adaptive strategies based on traffic data

### PhaseController
Controls the active phase and exposes traffic light states for incoming roads.

---

## 🔔 Event System

The simulation uses an event-driven architecture.

### SimulationEvent
An immutable event with:
- type (String)
- tick
- data map

### Emitted Events
- `VEHICLE_STOPPED` – vehicle transitions from moving to stopped
- `VEHICLE_WAITING` – vehicle remains stopped for a tick
- `VEHICLE_ARRIVED` – vehicle reaches its destination
- `VEHICLE_SPAWNED` (optional)

### EventPublisher
Interface for event sinks:
- console logger
- metrics collector
- test publisher

---

## 📊 Metrics & Evaluation

### MetricsCollector
Listens to simulation events and derives metrics such as:

- total simulation ticks
- number of vehicles arrived
- average travel time
- maximum travel time
- average waiting time
- total number of stops

### SimulationResult
Immutable result object returned after a simulation run.

---

## 🧪 Testing Strategy

The project uses **JUnit 5** and **AssertJ**.

### Test Types
- Unit tests (phases, controllers, strategies)
- Integration tests (vehicle movement, intersections)
- Event system tests (STOPPED / WAITING / ARRIVED)
- Metrics end-to-end tests

### Test Utilities
- `TestEventPublisher` for capturing emitted events
- Snapshot builders for deterministic setup

All critical system behavior is verified via tests.

---

## 🏗️ Architecture Highlights

- Separation of concerns:
  - Movement logic
  - Control logic
  - Observation (events)
  - Evaluation (metrics)
- No logging or metrics logic inside the movement engine
- Fully deterministic and reproducible simulations
- Easy to extend with new strategies or UIs

---

## 🚀 Example Usage (Pseudo-Code)

```java
        SimulationRunner runner = new SimulationRunner();
        SimulationResult result = runner.run(new SquareGraphFactory()); //Demo Graph
        System.out.println(result);
```
 🔮 Possible Extensions

The current architecture is designed to be extensible.  
Potential next steps and extensions include:

### 🚦 Traffic Control
- Adaptive traffic light strategies based on queue length or waiting time
- Sensor-based phase switching
- Priority handling for emergency vehicles
- Coordinated green waves across multiple intersections

### 📊 Simulation & Evaluation
- Strategy benchmarking framework
- Comparison of different phase strategies using identical traffic scenarios
- Extended metrics (throughput, fairness index, congestion levels)
- Export of simulation results (CSV, JSON)

### 🧠 Algorithms & Routing
- Dynamic route planning (e.g. Dijkstra, BFS)
- Rerouting based on congestion or red phases
- Multiple routing strategies per vehicle
- Probabilistic route selection

### 🖥️ Visualization & UI
- ASCII-based renderer for terminal output
- GUI-based visualization (JavaFX / Swing)
- Web frontend with REST API
- Replay mode using event logs

### 🗄️ Persistence & Analysis
- Event logging to files or databases
- SQL-based analysis of simulation runs
- Long-running simulations with historical comparison
- Integration with data analysis tools

### 🚗 Traffic Modeling
- Multi-lane roads
- Different vehicle types (cars, buses, emergency vehicles)
- Acceleration and deceleration modeling
- Pedestrians and public transport integration

### 🧪 Testing & Quality
- Property-based testing for movement invariants
- Stress tests with large graphs
- Deterministic replay tests
- Mutation testing for critical logic

---

These extensions can be developed incrementally without changing the core architecture,
thanks to the event-driven and modular design of the simulation engine.

