- first download javafx libray and make sure you download version 17 exaclty !!!!
https://gluonhq.com/products/javafx/


resources : https://www.geeksforgeeks.org/java/javafx-tutorial/


- then unzip it in the home directory:


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

* [ ] Draw horizontal and vertical roads (`Rectangle`).
* [ ] Draw the intersection center.
* [ ] Add traffic light placeholders (colored rectangles or circles).
* [ ] Position lights at all entry lanes (N, S, E, W).

✅ **Goal:** Crossroad drawn with visible traffic lights.
---

20:00
## 🕒 **PHASE 3 — Vehicles & Movement (4h)**
* [ ] Implement `Car` class:

  * [ ] Properties: position, direction, route, color, speed.
  * [ ] Method `update()` for movement.
* [ ] Handle **key input** (`KeyEvent`):

  * [ ] ↑ → car from South → North.
  * [ ] ↓ → car from North → South.
  * [ ] → → car from West → East.
  * [ ] ← → car from East → West.
  * [ ] `r` → random direction.
* [ ] Add random route (LEFT / RIGHT / STRAIGHT).
* [ ] Add cars to simulation list and update in loop.
* [ ] Implement **safe distance** (cars stop before next car).
* [ ] Remove cars exiting the screen.

✅ **Goal:** Cars spawn, move correctly, and respect spacing.

00:00
---
14:00
## 🕓 **PHASE 4 — Traffic Light System (3h)**

* [ ] Implement `TrafficLight`:

  * [ ] Fields: color (enum RED/GREEN), position, timer.
  * [ ] Methods: `switchLight()`, `update()`.
* [ ] Implement `TrafficController`:

  * [ ] Control all lights.
  * [ ] Define simple pattern: N–S green, then E–W green.
  * [ ] Optional: adaptive timing based on congestion.
* [ ] In car logic:

  * [ ] Stop at red near intersection.
  * [ ] Move on green.

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
