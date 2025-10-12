import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Simulation class is the core game loop of the Jraffic application.
 * It manages the state updates, car movements, and all other simulation events.
 */
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
        setupEnvironment();
    }

    public void start() {
        lastTime = System.nanoTime();
        timer = new AnimationTimer() {
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
        if (controller != null) {
            controller.update(dt);
        }
        // for (TrafficLight tl : lights) {
        //     tl.update(dt);
        // }
        updateCars(dt);
    }

    public void addCar(String arrowKey) {
        double speed = 200; // px/sec
        String route = randomRoute();

        double x = 0, y = 0;
        String moveDir = "";

        switch (arrowKey) {
            case "UP":
                x = 406;
                y = 800;
                moveDir = "N"; // from South, moving North
                break;
            case "DOWN":
                x = 354;
                y = 0;
                moveDir = "S"; // from North, moving South
                break;
            case "RIGHT":
                x = 0;
                y = 406;
                moveDir = "E"; // from West, moving East
                break;
            case "LEFT":
                x = 800;
                y = 354;
                moveDir = "W"; // from East, moving West
                break;
            default:
                return;
        }

        double safeDistance = 80;
        boolean canSpawn = true;

        for (Car other : cars) {
            if (other.getDirection().equals(moveDir)) {
                double dx = Math.abs(other.getX() - x);
                double dy = Math.abs(other.getY() - y);
                
                switch (moveDir) {
                    case "N":
                        if (other.getY() < y && dy < safeDistance && dx < 30) canSpawn = false;
                        break;
                    case "S":
                        if (other.getY() > y && dy < safeDistance && dx < 30) canSpawn = false;
                        break;
                    case "E":
                        if (other.getX() > x && dx < safeDistance && dy < 30) canSpawn = false;
                        break;
                    case "W":
                        if (other.getX() < x && dx < safeDistance && Math.abs(dy) < 30) canSpawn = false;
                        break;
                }
            }
        }

        if (!canSpawn) {
            return;
        }

        Car car = new Car(x, y, moveDir, speed, route);
        carsToAdd.add(car); // Queue car for safe addition
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
        if (!carsToAdd.isEmpty()) {
            for (Car c : carsToAdd) {
                cars.add(c);
                root.getChildren().add(c.getShape());
            }
            carsToAdd.clear();
        }

        double safeDistance = 80;
        carsToRemove.clear();

        for (Car car : cars) {
            boolean canMove = true;

            if (isApproachingIntersection(car)) {
                TrafficLight.State lightState = getRelevantLightStateFor(car);
                if (lightState == TrafficLight.State.RED) {
                    canMove = false;
                }
            }

            if (canMove) {
                for (Car other : cars) {
                    if (car == other) continue;
                    if (!car.getDirection().equals(other.getDirection())) continue;

                    double dx = other.getX() - car.getX();
                    double dy = other.getY() - car.getY();

                    switch (car.getDirection()) {
                        case "N":
                            if (dy < 0 && Math.abs(dy) < safeDistance && Math.abs(dx) < 20) canMove = false;
                            break;
                        case "S":
                            if (dy > 0 && dy < safeDistance && Math.abs(dx) < 20) canMove = false;
                            break;
                        case "E":
                            if (dx > 0 && dx < safeDistance && Math.abs(dy) < 20) canMove = false;
                            break;
                        case "W":
                            if (dx < 0 && Math.abs(dx) < safeDistance && Math.abs(dy) < 20) canMove = false;
                            break;
                    }

                    if (!canMove) break;
                }
            }

            if (canMove) {
                car.update(dt);
            }

            if (car.isOffScreen(800)) {
                carsToRemove.add(car);
            }
        }

        if (!carsToRemove.isEmpty()) {
            for (Car c : carsToRemove) {
                root.getChildren().remove(c.getShape());
            }
            cars.removeAll(carsToRemove);
        }
    }

    private boolean isApproachingIntersection(Car car) {
        double frontX = car.getX();
        double frontY = car.getY();
        String dir = car.getFromDirection();

        switch (dir) {
            case "S": // From North, moving South
                frontY = car.getY() + 40;
                return frontY > 345 && frontY < 350;
            case "N": // From South, moving North
                frontY = car.getY();
                return frontY > 450 && frontY < 455;
            case "W": // From East, moving West
                frontX = car.getX() ;
                return frontX > 450 && frontX < 455;
            case "E": // From West, moving East
                frontX = car.getX() + 40;
                return frontX > 345 && frontX < 350;
            default:
                return false;
        }
    }

    private TrafficLight.State getRelevantLightStateFor(Car car) {
        switch (car.getFromDirection()) {
            case "W":
                return lights.get(0).getState(); // tlNorth
            case "E":
                return lights.get(1).getState(); // tlSouth
            case "N":
                return lights.get(2).getState(); // tlEast
            case "S":
                return lights.get(3).getState(); // tlWest
            default:
                return TrafficLight.State.RED;
        }
    }

    private void setupEnvironment() {
        Rectangle horizontal = new Rectangle(0, 350, 800, 100);
        horizontal.setFill(Color.GRAY);
        Rectangle vertical = new Rectangle(350, 0, 100, 800);
        vertical.setFill(Color.GRAY);

        root.getChildren().addAll(horizontal, vertical);

        Line h_divider = new Line(0, 400, 800, 400);
        h_divider.getStrokeDashArray().addAll(10.0, 10.0);
        h_divider.setStroke(Color.WHITE);
        h_divider.setStrokeWidth(3);

        Line v_divider = new Line(400, 0, 400, 800);
        v_divider.getStrokeDashArray().addAll(10.0, 10.0);
        v_divider.setStroke(Color.WHITE);
        v_divider.setStrokeWidth(3);

        root.getChildren().addAll(h_divider, v_divider);

        TrafficLight tlWest = new TrafficLight(330, 330, TrafficLight.State.GREEN);
        TrafficLight tlEast = new TrafficLight(470, 470, TrafficLight.State.RED);
        TrafficLight tlNorth = new TrafficLight(470, 330, TrafficLight.State.RED);
        TrafficLight tlSouth = new TrafficLight(330, 470, TrafficLight.State.RED);

        lights.add(tlNorth);
        lights.add(tlSouth);
        lights.add(tlEast);
        lights.add(tlWest);

        for (TrafficLight tl : lights) {
            root.getChildren().add(tl.getShape());
        }

        controller = new TrafficController(lights, cars);
    }
}