## **Running the Project**

1. **Download JavaFX SDK (version 17)**
   Make sure to download **exactly version 17**:
   [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

2. **Unzip the SDK**
   Extract the downloaded archive into your home directory (or any preferred location). For example:

   ```bash
   ~/javafx-sdk-17
   ```

3. **Run the project using the provided script**
   The `run.sh` script handles compilation and JavaFX module paths automatically:

   ```bash
   ./run.sh
   ```

> ⚠️ Important: The project has been tested with **JavaFX 17** only. Using a different version may cause compilation or runtime errors.




# ✅ **Jraffic Project — 16-Hour TODO Plan**

---
16:00
## 🕐 **PHASE 1 — Setup & Structure (2h)**

* [X] Create a new **JavaFX project**.
* [X] Implement main class `App` extending `Application`.
* [X] Create scene (≈800×800) and add a `Pane` or `Group` for drawing.
* [X] Create core classes:

  * [X] `Car`
  * [X] `TrafficLight`
  * [X] `Road`
  * [X] `Intersection`
  * [X] `TrafficController`
  * [X] `Simulation` (or main update loop)
* [X] Add constants file (`Constants.java`) for sizes, speeds, timing, etc.
* [X] Create main animation loop using `Timeline` or `AnimationTimer`.

✅ **Goal:** Empty window + class skeletons + update loop running.
17:30 ?? 18:23
---
18:00
## 🕑 **PHASE 2 — Draw Environment (2h)**

* [X] Draw horizontal and vertical roads (`Rectangle`).
* [X] Draw the intersection center.
* [X] Add traffic light placeholders (colored rectangles or circles).
* [X] Position lights at all entry lanes (N, S, E, W).

✅ **Goal:** Crossroad drawn with visible traffic lights.
---

20:00
## 🕒 **PHASE 3 — Vehicles & Movement (4h)**
* [X] Implement `Car` class:

  * [X] Properties: position, direction, route, color, speed.
  * [X] Method `update()` for movement.
* [X] Handle **key input** (`KeyEvent`):

  * [X] ↑ → car from South → North.
  * [X] ↓ → car from North → South.
  * [X] → → car from West → East.
  * [X] ← → car from East → West.
  * [X] `r` → random direction.
* [X] Add random route (LEFT / RIGHT / STRAIGHT).
* [X] Add cars to simulation list and update in loop.
* [X] Implement **safe distance** (cars stop before next car).
* [X] Remove cars exiting the screen.

✅ **Goal:** Cars spawn, move correctly, and respect spacing.

00:00
---
01:23 😴 💤
## 🕓 **PHASE 4 — Traffic Light System (3h)**

* [X] Implement `TrafficLight`:

  * [X] Fields: color (enum RED/GREEN), position, timer.
  * [X] Methods: `switchLight()`, `update()`.
* [X] Implement `TrafficController`:

  * [X] Control all lights.
  * [X] Define simple pattern: N–S green, then E–W green.
  * [ ] Optional: adaptive timing based on congestion.
* [ ] In car logic:

  * [X] Stop at red near intersection.
  * [X] Move on green.

✅ **Goal:** Lights switch automatically, cars obey signals.

---
17:00
## 🕔 **PHASE 5 — Dynamic Congestion Handling (2h)**

* [ ] Each lane tracks number of cars.
* [ ] Compute lane capacity:
  `capacity = floor(lane_length / (vehicle_length + safety_gap))`
* [ ] If lane is full:

  * [ ] Extend green time for that lane.
  * [ ] Delay cycle for less busy lanes.

✅ **Goal:** Traffic adapts to congestion.

---
19:00
## 🕕 **PHASE 6 — Controls, Colors & Polish (2h)**

* [ ] Prevent spamming car creation (cooldown per lane).
* [ ] Assign car colors per route:

  * [ ] Left = Blue
  * [ ] Right = Yellow
  * [ ] Straight = Red
* [ ] Smooth animation (use transitions or interpolation).
* [ ] Add small on-screen labels for directions or queue sizes.
* [ ] Add key `Esc` to exit simulation cleanly.

✅ **Goal:** Functional, user-friendly simulation.

---
21:00
## 🕖 **PHASE 7 — Testing & Cleanup (1h)**

* [ ] Test each direction and route.
* [ ] Confirm cars stop/go correctly at lights.
* [ ] Confirm congestion logic behaves.
* [ ] Remove debug prints and unused code.
* [ ] Add clear comments to major classes and methods.

✅ **Goal:** Stable, readable, ready-to-submit project.
22:00



## 🕖 ** Bug Fixing
* [ ] 
* [ ] improve light algorithm to prevent jam (other improvements)
* [ ] green car comming from east has weird behaviour in intersection
* [ ] improve safty distance : optional
