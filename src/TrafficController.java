import java.util.*;

public class TrafficController {
    private final List<TrafficLight> lights;
    private final List<Car> cars;
    private final Random random = new Random();
    private static final List<String> DIRECTIONS = Arrays.asList("N", "S", "E", "W");
    
    private String currentGreen = null;
    private double time = 0.0;
    private double greenStartTime = 0.0;
    private boolean transitioning = false;
    private double transitionStart = 0.0;
    private String nextGreen = null;
    
    private final Map<String, Double> firstCarTime = new HashMap<>();
    private final Map<String, Boolean> hasFirstCar = new HashMap<>();
    private final Map<String, Double> lastGreenTime = new HashMap<>();

    public TrafficController(List<TrafficLight> lights, List<Car> cars) {
        this.lights = lights;
        this.cars = cars;
        initialize();
    }
    
    private void initialize() {
        setAllRed();
        for (String dir : DIRECTIONS) {
            lastGreenTime.put(dir, 0.0);
            firstCarTime.put(dir, 0.0);
            hasFirstCar.put(dir, false);
        }
    }
    
    public void update(double dt) {
        time += dt;
        trackCars();
        
        if (transitioning) {
            if (time - transitionStart >= 0.5) {
                transitioning = false;
                if (nextGreen != null) setGreen(nextGreen);
                nextGreen = null;
            }
        } else if (currentGreen != null) {
            handleGreen();
        } else {
            String next = selectNext();
            if (next != null) setGreen(next);
        }
    }
    
    private void handleGreen() {
        int cars = countCars(currentGreen);
        double duration = time - greenStartTime;
        
        if (duration >= 8.0 || (cars == 0 && duration >= 3.0)) {
            startTransition();
        }
    }
    
    private void startTransition() {
        if (currentGreen != null) {
            lastGreenTime.put(currentGreen, time);
            resetTracking(currentGreen);
        }
        
        setAllRed();
        currentGreen = null;
        transitioning = true;
        transitionStart = time;
        nextGreen = selectNext();
        
        if (nextGreen == null) transitioning = false;
    }
    
    private void setGreen(String dir) {
        int idx = switch (dir) {
            case "N" -> 2; case "S" -> 3; case "E" -> 1; case "W" -> 0; default -> -1;
        };
        if (idx != -1) lights.get(idx).setState(TrafficLight.State.GREEN);
        currentGreen = dir;
        greenStartTime = time;
    }
    
    private String selectNext() {
        Map<String, Double> scores = new HashMap<>();
        for (String dir : DIRECTIONS) {
            if (isEligible(dir)) scores.put(dir, calculateScore(dir));
        }
        
        double max = 0;
        List<String> tops = new ArrayList<>();
        for (Map.Entry<String, Double> e : scores.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                tops.clear();
                tops.add(e.getKey());
            } else if (e.getValue() == max) tops.add(e.getKey());
        }
        
        return tops.isEmpty() ? null : tops.get(random.nextInt(tops.size()));
    }
    
    private boolean isEligible(String dir) {
        double lastGreen = lastGreenTime.get(dir);
        return !(lastGreen > 0 && time - lastGreen < 1.5) && countCars(dir) > 0;
    }
    
    private double calculateScore(String dir) {
        int count = countCars(dir);
        double wait = getWaitTime(dir);
        
        double capacityScore = 0.35 * (count / 5.0) * 100;
        double waitScore = 0.45 * (Math.min(wait / 15.0, 1.0)) * 100;
        
        if (wait > 12.0) waitScore += 25;
        if (wait > 20.0) waitScore += 20;
        
        return capacityScore + waitScore;
    }
    
    private void trackCars() {
        for (String dir : DIRECTIONS) {
            int count = countCars(dir);
            boolean detected = hasFirstCar.get(dir);
            
            if (count > 0 && !detected) {
                hasFirstCar.put(dir, true);
                firstCarTime.put(dir, time);
            }
            if (count == 0 && detected) resetTracking(dir);
        }
    }
    
    private void resetTracking(String dir) {
        hasFirstCar.put(dir, false);
        firstCarTime.put(dir, 0.0);
    }
    
    private double getWaitTime(String dir) {
        return hasFirstCar.get(dir) ? time - firstCarTime.get(dir) : 0.0;
    }
    
    private int countCars(String dir) {
        int count = 0;
        for (Car car : cars) {
            if (car.getFromDirection().equals(dir) && onRoad(car)) count++;
        }
        return count;
    }
    
    private boolean onRoad(Car car) {
        double x = car.getX(), y = car.getY();
        return switch (car.getFromDirection()) {
            case "N" -> y > 350;
            case "S" -> y < 450;
            case "E" -> x < 450;
            case "W" -> x > 350;
            default -> false;
        };
    }
    
    private void setAllRed() {
        for (TrafficLight light : lights) {
            light.setState(TrafficLight.State.RED);
        }
    }
    
    public String getCurrentGreen() { return currentGreen; }
    public double getGreenTime() { return currentGreen != null ? time - greenStartTime : 0; }
}