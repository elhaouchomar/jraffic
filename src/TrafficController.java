import java.util.List;

public class TrafficController {
    private final List<TrafficLight> lights; // all 4 lights: [North, South, East, West]
    private final List<Car> cars;

    private String currentGreenDirection = null; // N, S, E, or W

    public TrafficController(List<TrafficLight> ns, List<TrafficLight> ew, List<Car> cars) {
        this.lights = List.of(ns.get(0), ns.get(1), ew.get(0), ew.get(1)); // N, S, E, W
        this.cars = cars;
        setAllRed(); // Start with all lights red
    }

    public void update(double dt) {
        if (currentGreenDirection != null) {
            // A light is green, check if it should be turned red
            if (countActiveCarsOnRoad(currentGreenDirection) == 0) {
                System.out.println("🚫 Road " + currentGreenDirection + " is clear. Setting all lights to red.");
                setAllRed();
                currentGreenDirection = null;
            }
        } else {
            // All lights are red, find the busiest road to turn green
            String busiestDirection = getBusiestDirection();
            if (busiestDirection != null) {
                System.out.println("🟢 Turning light green for " + busiestDirection + " road.");
                setGreen(busiestDirection);
                currentGreenDirection = busiestDirection;
            }
        }
    }

    private String getBusiestDirection() {
        int northWaiting = countActiveCarsOnRoad("N");
        int southWaiting = countActiveCarsOnRoad("S");
        int eastWaiting = countActiveCarsOnRoad("E");
        int westWaiting = countActiveCarsOnRoad("W");

        int maxWaiting = Math.max(Math.max(northWaiting, southWaiting), Math.max(eastWaiting, westWaiting));

        if (maxWaiting == 0) {
            return null; // No cars waiting anywhere
        }

        // Prioritize in order N -> S -> E -> W in case of a tie
        if (northWaiting == maxWaiting) {
            return "N";
        } else if (southWaiting == maxWaiting) {
            return "S";
        } else if (eastWaiting == maxWaiting) {
            return "E";
        } else {
            return "W";
        }
    }

    private void setAllRed() {
        for (TrafficLight tl : lights) {
            tl.setState(TrafficLight.State.RED);
        }
    }

    private void setGreen(String direction) {
        int index = getIndexForDirection(direction);
        if (index != -1) {
            lights.get(index).setState(TrafficLight.State.GREEN);
        }
    }

    private int getIndexForDirection(String direction) {
        return switch (direction) {
            case "N" -> 0;
            case "S" -> 1;
            case "E" -> 2;
            case "W" -> 3;
            default -> -1;
        };
    }

    private int countActiveCarsOnRoad(String direction) {
        int count = 0;
        for (Car car : cars) {
            if (car.getDirection().equals(direction) && isCarOnRoad(car)) {
                count++;
            }
        }
        return count;
    }

    private boolean isCarOnRoad(Car car) {
        double x = car.getX();
        double y = car.getY();
        String dir = car.getDirection();

        // Define zones that cover the car's path until it exits the screen
        switch (dir) {
            case "N": // Moving up
                return y > 350;
            case "S": // Moving down
                return y < 450;
            case "E": // Moving right
                return x < 450;
            case "W": // Moving left
                return x > 350;
            default:
                return false;
        }
    }
}
