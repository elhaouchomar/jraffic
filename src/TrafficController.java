import java.util.*;

public class TrafficController {
    private final List<TrafficLight> lights;
    private final List<Car> cars;
    private final Random random = new Random();
    
    private static final List<String> DIRECTIONS = Arrays.asList("N", "S", "E", "W");
    private static final int BASE_MAX_CAPACITY = 5;
    
    private static final double CAPACITY_WEIGHT = 0.35;
    private static final double WAITING_WEIGHT = 0.45;
    private static final double EMERGENCY_WEIGHT = 0.20;
    
    private static final double MAX_GREEN_TIME_SECONDS = 8.0;
    private static final double MIN_GREEN_TIME_SECONDS = 3.0;
    private static final double TRANSITION_DELAY_SECONDS = 0.5;
    private static final double COOLDOWN_PERIOD_SECONDS = 1.5;
    private static final double STARVATION_THRESHOLD_SECONDS = 12.0;
    private static final double EMERGENCY_THRESHOLD_SECONDS = 20.0;
    
    private String currentGreenDirection = null;
    private double simulationTime = 0.0;
    private double currentGreenStartTime = 0.0;
    private boolean inTransition = false;
    private double transitionStartTime = 0.0;
    private String nextGreenDirection = null;
    
    private final Map<String, Integer> directionMaxCapacity = new HashMap<>();
    private final Map<String, Double> firstCarArrivalTime = new HashMap<>();
    private final Map<String, Boolean> firstCarDetected = new HashMap<>();
    private final Map<String, Double> directionLastGreenTime = new HashMap<>();

    public TrafficController(List<TrafficLight> lights, List<Car> cars) {
        this.lights = lights;
        this.cars = cars;
        initializeSystem();
    }
    
    private void initializeSystem() {
        setAllRed();
        
        for (String direction : DIRECTIONS) {
            directionMaxCapacity.put(direction, BASE_MAX_CAPACITY);
            directionLastGreenTime.put(direction, 0.0);
            firstCarArrivalTime.put(direction, 0.0);
            firstCarDetected.put(direction, false);
        }
    }
    
    public void update(double dt) {
        simulationTime += dt;
        trackFirstCarArrivals();
        
        if (inTransition) {
            handleTransitionPhase();
        } else if (currentGreenDirection != null) {
            handleActiveGreenLight();
        } else {
            activateNextGreenLight();
        }
    }
    
    private void handleTransitionPhase() {
        if (simulationTime - transitionStartTime >= TRANSITION_DELAY_SECONDS) {
            completeTransition();
        }
    }
    
    private void completeTransition() {
        inTransition = false;
        if (nextGreenDirection != null) {
            activateGreenLight(nextGreenDirection);
        }
        nextGreenDirection = null;
    }
    
    private void handleActiveGreenLight() {
        int carCount = countCarsInDirection(currentGreenDirection);
        double greenDuration = simulationTime - currentGreenStartTime;
        
        if (greenDuration >= MAX_GREEN_TIME_SECONDS) {
            startTransition();
        } else if (carCount == 0 && greenDuration >= MIN_GREEN_TIME_SECONDS) {
            startTransition();
        }
    }
    
    private void startTransition() {
        if (currentGreenDirection != null) {
            directionLastGreenTime.put(currentGreenDirection, simulationTime);
            resetDirectionTracking(currentGreenDirection);
        }
        
        setAllRed();
        currentGreenDirection = null;
        inTransition = true;
        transitionStartTime = simulationTime;
        nextGreenDirection = selectNextDirection();
        
        if (nextGreenDirection == null) {
            inTransition = false;
        }
    }
    
    private void activateNextGreenLight() {
        String nextDirection = selectNextDirection();
        if (nextDirection != null) {
            activateGreenLight(nextDirection);
        }
    }
    
    private void activateGreenLight(String direction) {
        setGreen(direction);
        currentGreenDirection = direction;
        currentGreenStartTime = simulationTime;
    }
    
    private String selectNextDirection() {
        Map<String, Double> priorities = new HashMap<>();
        
        for (String direction : DIRECTIONS) {
            if (isDirectionEligible(direction)) {
                priorities.put(direction, calculateDirectionPriority(direction));
            }
        }
        
        return findWinner(priorities);
    }
    
    private boolean isDirectionEligible(String direction) {
        double lastGreen = directionLastGreenTime.get(direction);
        double elapsed = simulationTime - lastGreen;
        if (lastGreen > 0 && elapsed < COOLDOWN_PERIOD_SECONDS) {
            return false;
        }
        return countCarsInDirection(direction) > 0;
    }
    
    private double calculateDirectionPriority(String direction) {
        int carCount = countCarsInDirection(direction);
        double waitTime = calculateWaitingTime(direction);
        
        double capacityScore = CAPACITY_WEIGHT * ((double) carCount / BASE_MAX_CAPACITY) * 100;
        double waitScore = WAITING_WEIGHT * (Math.min(waitTime / 15.0, 1.0)) * 100;
        double emergencyScore = 0;
        
        if (waitTime > STARVATION_THRESHOLD_SECONDS) {
            waitScore += 25;
        }
        if (waitTime > EMERGENCY_THRESHOLD_SECONDS) {
            emergencyScore = EMERGENCY_WEIGHT * 100;
        }
        
        return capacityScore + waitScore + emergencyScore;
    }
    
    private String findWinner(Map<String, Double> priorities) {
        double maxPriority = 0;
        List<String> topCandidates = new ArrayList<>();
        
        for (Map.Entry<String, Double> entry : priorities.entrySet()) {
            if (entry.getValue() > maxPriority) {
                maxPriority = entry.getValue();
                topCandidates.clear();
                topCandidates.add(entry.getKey());
            } else if (entry.getValue() == maxPriority) {
                topCandidates.add(entry.getKey());
            }
        }
        
        if (topCandidates.isEmpty()) return null;
        return topCandidates.get(random.nextInt(topCandidates.size()));
    }
    
    private void trackFirstCarArrivals() {
        for (String direction : DIRECTIONS) {
            int count = countCarsInDirection(direction);
            boolean detected = firstCarDetected.get(direction);
            
            if (count > 0 && !detected) {
                firstCarDetected.put(direction, true);
                firstCarArrivalTime.put(direction, simulationTime);
            }
            
            if (count == 0 && detected) {
                resetDirectionTracking(direction);
            }
        }
    }
    
    private void resetDirectionTracking(String direction) {
        firstCarDetected.put(direction, false);
        firstCarArrivalTime.put(direction, 0.0);
    }
    
    private double calculateWaitingTime(String direction) {
        if (!firstCarDetected.get(direction)) return 0.0;
        return simulationTime - firstCarArrivalTime.get(direction);
    }
    
    private List<Car> getCarsInDirection(String direction) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.getFromDirection().equals(direction) && isCarOnRoad(car)) {
                result.add(car);
            }
        }
        return result;
    }
    
    private int countCarsInDirection(String direction) {
        return getCarsInDirection(direction).size();
    }
    
    private boolean isCarOnRoad(Car car) {
        double x = car.getX();
        double y = car.getY();
        String dir = car.getFromDirection();
        
        switch (dir) {
            case "N": return y > 350;
            case "S": return y < 450;
            case "E": return x < 450;
            case "W": return x > 350;
            default: return false;
        }
    }
    
    private void setAllRed() {
        for (TrafficLight light : lights) {
            light.setState(TrafficLight.State.RED);
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
            case "N" -> 2;
            case "S" -> 3;
            case "E" -> 1;
            case "W" -> 0;
            default -> -1;
        };
    }
    
    public String getCurrentGreenDirection() {
        return currentGreenDirection;
    }
    
    public double getCurrentGreenTime() {
        return currentGreenDirection != null ? simulationTime - currentGreenStartTime : 0;
    }
}