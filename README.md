# 🚦 jraffic

A simple **traffic simulation** built in **Java** using **JavaFX**.
This project simulates a road intersection with vehicles, traffic lights, and adaptive control logic to prevent congestion.

---

## 🧭 Overview

The goal of **jraffic** is to model traffic flow through an intersection and dynamically control traffic lights to minimize congestion.
It visualizes cars moving through two intersecting roads, obeying traffic lights and basic traffic rules.

---

## 🧩 Features

* Two intersecting roads (North–South and East–West)
* Each direction supports:

  * Straight movement
  * Left turn
  * Right turn
* Dynamic traffic light control:

  * Green/red signals only
  * Adapts to lane congestion using:

    ```
    capacity = floor(lane_length / (vehicle_length + safety_gap))
    ```
* Vehicles:

  * Fixed speed per car
  * Maintain safe following distance
  * Stop at red lights
  * Colored by route type (right, left, straight)
* Keyboard-controlled car spawning with safe spacing

---

## 🎮 Controls

| Key | Action                                         |
| --- | ---------------------------------------------- |
| ↑   | Spawn a vehicle moving north → south           |
| ↓   | Spawn a vehicle moving south → north           |
| →   | Spawn a vehicle moving west → east             |
| ←   | Spawn a vehicle moving east → west             |
| r   | Spawn a random vehicle from a random direction |
| Esc | Exit the simulation                            |

> Vehicles spawn only if there is enough safe distance from existing ones.

---

## 🧠 Simulation Logic

* Traffic lights are placed at each intersection entry.
* Each vehicle follows a **fixed route** and color.
* The **TrafficController** manages light timing based on congestion.
* **Simulation** handles vehicle updates and rendering.
* **Car** class defines vehicle movement and rules.
* **TrafficLight** handles light state transitions.
* **App** starts the JavaFX interface.

---

## 📁 Project Structure

```
src/
├── App.java                # Entry point (JavaFX Application)
├── Car.java                # Vehicle class and movement logic
├── Simulation.java         # Main simulation loop and rendering
├── TrafficController.java  # Traffic light timing and logic
├── TrafficLight.java       # Traffic light states
├── build/                  # Compiled .class files
└── run.sh                  # Script to compile and run with JavaFX
```

---

## ⚙️ Running the Project

### 1. Install JavaFX 17

Download **JavaFX SDK 17** from:
[https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

### 2. Extract the SDK

Unzip it somewhere convenient, for example:

```bash
~/javafx-sdk-17
```

### 3. Run the Simulation

Use the provided script:

```bash
./run.sh
```

The script automatically handles compilation and JavaFX module paths.

> ⚠️ Tested only with **JavaFX 17**.
> Other versions may cause errors or crashes.

---

## 🧮 Example Visualization

```
                        North
                    |  ↓  |  ↑  |
                    |  ↓  |  ↑  |
     _______________|     |     |_______________
     ← ←                                     ← ←
East ---------------             --------------- West
     → →                                     → →
     _______________             _______________
                    |  ↓  |  ↑  |
                    |  ↓  |  ↑  |
                        South
```

---

## 🧱 Requirements

* **Java 17+**
* **JavaFX 17 SDK**

---

## 🧑‍💻 Authors

Project by **obenali** **oelhaouc** **zabdelal** 
Zone01 Oujda — *Traffic Control Simulation Project*

---

## 📝 License

This project is for educational purposes under the Zone01 curriculum.
You are free to modify and extend it as part of your learning.

--- 

## 💬 Feedback & Contributions

If you find a bug or have an idea to improve **jraffic**, feel free to open an issue or suggest changes.
All constructive feedback is welcome — this project is meant to evolve and get better over time.



