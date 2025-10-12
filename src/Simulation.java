
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The Simulation class in your JavaFX application serves as the core game loop.
// It manages the continuous updating of the simulation's state, ensuring that
// the a
// pplication remains responsive and that animations proceed smoothly.

// https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html
// The class AnimationTimer allows to create a timer, that is called in each frame while it is active.
public class Simulation {
    private Pane root;
    private AnimationTimer timer;
    private long lastTime;

    private List<Car> cars = new ArrayList<>();
    private final List<Car> carsToAdd = new ArrayList<>();
    private final List<Car> carsToRemove = new ArrayList<>();

    private List<TrafficLight> lights = new ArrayList<>();
    private TrafficController controller;

    private Random random = new Random();

    public Simulation(Pane root) {
        this.root = root;
        setupEnvironment(); // draw roads, intersection, and traffic lights
    }

    public void start() {
        lastTime = System.nanoTime(); // https://www.tutorialspoint.com/java/lang/system_nanotime.htm
        timer = new AnimationTimer() { // anonymous class instead of extends and override and new
                                       // https://docs.oracle.com/javase/8/javafx/api/javafx/animation/AnimationTimer.html

            @Override
            public void handle(long now) {
                double dt = (now - lastTime) / 1e9;
                update(dt);
                lastTime = now;
            }
        };
        timer.start();
    }

    private void update(double dt) {
        if (controller != null)
            controller.update(dt);
        for (TrafficLight tl : lights)
            tl.update(dt);
        updateCars(dt);
    }

    /// --->
    public void addCar(String arrowKey) {
        double speed = 200; // px/sec
        String route = randomRoute();

        Color[] colors = { Color.RED, Color.BLUE, Color.LIMEGREEN, Color.YELLOW };
        Color color = colors[random.nextInt(colors.length)];

        double x = 0, y = 0;
        String moveDir = "";

        // --- Accurate spawn coordinates ---
        switch (arrowKey) {
            case "UP":
                x = 406;
                y = 800;
                moveDir = "N";
                break; // from south → up
            case "DOWN":
                x = 354;
                y = 0;
                moveDir = "S";
                break; // from north → down
            case "RIGHT":
                x = 0;
                y = 406;
                moveDir = "E";
                break; // from west → right
            case "LEFT":
                x = 800;
                y = 354;
                moveDir = "W";
                break; // from east → left
            default:
                return;
        }

        // --- Safety check before adding ---
        double safeDistance = 80; // pixels between cars
        boolean canSpawn = true;

        for (Car other : cars) {
            if (other.getDirection().equals(moveDir)) {
                double dx = Math.abs(other.getX() - x);
                double dy = Math.abs(other.getY() - y);

                switch (moveDir) {
                    case "N":
                        if (other.getY() < y && dy < safeDistance && dx < 30)
                            canSpawn = false;
                        break;
                    case "S":
                        if (other.getY() > y && dy < safeDistance && dx < 30)
                            canSpawn = false;
                        break;
                    case "E":
                        if (other.getX() > x && dx < safeDistance && dy < 30)
                            canSpawn = false;
                        break;
                    case "W":
                        if (other.getX() < x && dx < safeDistance && Math.abs(dy) < 30)
                            canSpawn = false;
                        break;
                }
            }
        }

        if (!canSpawn) {
            // System.out.println("❌ Not enough space to add a new car on " + moveDir + "
            // lane.");
            return;
        }

        // --- Safe to add the car ---
        Car car = new Car(x, y, moveDir, color, speed, route);
        carsToAdd.add(car); // queue it instead of adding immediately
    }

    public void addCarRandom() {
        String[] keys = { "UP", "DOWN", "LEFT", "RIGHT" };
        String randomKey = keys[random.nextInt(keys.length)];
        addCar(randomKey);
    }

    private String randomRoute() {
        String[] routes = { "LEFT", "RIGHT", "STRAIGHT" };
        return routes[random.nextInt(routes.length)];
    }

    private void updateCars(double dt) {
        // Safely add cars from queue
        if (!carsToAdd.isEmpty()) {
            for (Car c : carsToAdd) {
                cars.add(c);
                root.getChildren().add(c.getShape());
            }
            carsToAdd.clear();
        }

        double safeDistance = 50;
        carsToRemove.clear();

        for (Car car : cars) {
            boolean canMove = true;

            // ---- 1️⃣ Red light check ----
            if (isApproachingIntersection(car)) {
                TrafficLight.State lightState = getRelevantLightStateFor(car);
                if (lightState == TrafficLight.State.RED) {
                    canMove = false;
                    System.out.println("car stopped because of light");
                }
            }

            // ---- 2️⃣ Safe distance check ----
            if (canMove) {
                for (Car other : cars) {
                    if (car == other)
                        continue;
                    if (!car.getDirection().equals(other.getDirection()))
                        continue;

                    double dx = other.getX() - car.getX();
                    double dy = other.getY() - car.getY();

                    switch (car.getDirection()) {
                        case "N":
                            if (dy < 0 && Math.abs(dy) < safeDistance && Math.abs(dx) < 20)
                                canMove = false;
                            break;
                        case "S":
                            if (dy > 0 && dy < safeDistance && Math.abs(dx) < 20)
                                canMove = false;
                            break;
                        case "E":
                            if (dx > 0 && dx < safeDistance && Math.abs(dy) < 20)
                                canMove = false;
                            break;
                        case "W":
                            if (dx < 0 && Math.abs(dx) < safeDistance && Math.abs(dy) < 20)
                                canMove = false;
                            break;
                    }

                    if (!canMove)
                        break;
                }
            }

            // ---- 3️⃣ Update position if allowed ----
            if (canMove)
                car.update(dt);

            // ---- 4️⃣ Remove cars off-screen ----
            if (car.isOffScreen(800))
                carsToRemove.add(car);
        }

        // ---- 5️⃣ Safe removal ----
        if (!carsToRemove.isEmpty()) {
            for (Car c : carsToRemove)
                root.getChildren().remove(c.getShape());
            cars.removeAll(carsToRemove);
        }
    }

    private boolean isApproachingIntersection(Car car) {
        double frontX = car.getX();
        double frontY = car.getY();
        String dir = car.getDirection();

        switch (dir) {
            case "S":
                // commint from nort moving down
                frontY = car.getY() + 40;
                if (frontY > 350) return false;
                return frontY > 345 && frontY < 350; // stop 50px before center
            case "N":
                // moving down: bottom edge is front
                frontY = car.getY();
                return frontY > 450 && frontY < 455;
            case "W":
                // moving right: right edge is front
                frontX = car.getX() ;
                return frontX > 450 && frontX < 455;
            case "E":
                // moving left: left edge is front
                frontX = car.getX() + 40;
                return frontX > 345 && frontX < 350;
            default:
                return false;
        }
    }

    private TrafficLight.State getRelevantLightStateFor(Car car) {
        switch (car.getDirection()) {
            case "N":
                return lights.get(0).getState(); // tlNorth (index 0)
            case "S":
                return lights.get(1).getState(); // tlSouth (index 1)
            case "E":
                return lights.get(2).getState(); // tlEast (index 2)
            case "W":
                return lights.get(3).getState(); // tlWest (index 3)
            default:
                return TrafficLight.State.RED;
        }
    }

    private void setupEnvironment() {
        // --- Roads ---
        Rectangle horizontal = new Rectangle(0, 350, 800, 100); // y=350, height=100
        horizontal.setFill(Color.GRAY);
        Rectangle vertical = new Rectangle(350, 0, 100, 800); // x=350, width=100
        vertical.setFill(Color.GRAY);

        root.getChildren().addAll(horizontal, vertical);

        // --- Lane dividers ---

        // North ↔ West
        Line nwLine1 = new Line(0, 400, 800, 400); // top-left diagonal / adjust as needed
        nwLine1.getStrokeDashArray().addAll(10.0, 10.0); // 10px dash, 10px gap
        nwLine1.setStroke(Color.WHITE);
        nwLine1.setStrokeWidth(3);

        // East ↔ South
        Line esLine1 = new Line(400, 0, 400, 800); // top-right diagonal / adjust as needed
        esLine1.getStrokeDashArray().addAll(10.0, 10.0);
        esLine1.setStroke(Color.WHITE);
        esLine1.setStrokeWidth(3);

        root.getChildren().addAll(nwLine1, esLine1);

        // --- Traffic Lights (real TrafficLight objects) ---
        TrafficLight tlNorth = new TrafficLight(330, 330, TrafficLight.State.GREEN);
        TrafficLight tlSouth = new TrafficLight(470, 470, TrafficLight.State.GREEN);
        TrafficLight tlEast = new TrafficLight(470, 330, TrafficLight.State.RED);
        TrafficLight tlWest = new TrafficLight(330, 470, TrafficLight.State.RED);

        // add to local list and to root for display
        lights.add(tlNorth);
        lights.add(tlSouth);
        lights.add(tlEast);
        lights.add(tlWest);

        // add shapes to scene graph
        for (TrafficLight tl : lights)
            root.getChildren().add(tl.getShape());

        // create NS and EW groups and controller
        List<TrafficLight> ns = List.of(tlNorth, tlSouth);
        List<TrafficLight> ew = List.of(tlEast, tlWest);
        controller = new TrafficController(ns, ew, cars);

    }

}
