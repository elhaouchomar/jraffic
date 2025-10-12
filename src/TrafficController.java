import java.util.List;

public class TrafficController {
    private final List<TrafficLight> northSouth;
    private final List<TrafficLight> eastWest;
    private final List<Car> cars; // cars in simulation

    private double timer = 0;
    private double cycle = 6.0; // seconds per phase
    private boolean nsGreen = true;

    public TrafficController(List<TrafficLight> ns, List<TrafficLight> ew, List<Car> cars) {
        this.northSouth = ns;
        this.eastWest = ew;
        this.cars = cars; // initialize cars
        setPhase(true);
    }

    // Map TrafficLight to its direction
    private String tlDirection(TrafficLight tl) {
        if (northSouth.contains(tl)) {
            return northSouth.indexOf(tl) == 0 ? "N" : "S";
        } else {
            return eastWest.indexOf(tl) == 0 ? "E" : "W";
        }
    }

    private boolean isIntersectionClear(String dir) {
        for (Car car : cars) {
            switch (dir) {
                case "N":
                    if (car.getDirection().equals("N") && car.getY() < 450 && car.getY() > 350)
                        return false;
                    break;
                case "S":
                    if (car.getDirection().equals("S") && car.getY() > 350 && car.getY() < 450)
                        return false;
                    break;
                case "E":
                    if (car.getDirection().equals("E") && car.getX() > 350 && car.getX() < 450)
                        return false;
                    break;
                case "W":
                    if (car.getDirection().equals("W") && car.getX() < 450 && car.getX() > 350)
                        return false;
                    break;
            }
        }
        return true;
    }

    public void update(double dt) {
        timer += dt;
        if (timer >= cycle) {
            boolean canSwitch = true;
            if (nsGreen) { // switching to E–W green
                for (TrafficLight tl : northSouth) {
                    if (!isIntersectionClear(tlDirection(tl))) {
                        canSwitch = false;
                        break;
                    }
                }
            } else { // switching to N–S green
                for (TrafficLight tl : eastWest) {
                    if (!isIntersectionClear(tlDirection(tl))) {
                        canSwitch = false;
                        break;
                    }
                }
            }

            if (canSwitch) {
                nsGreen = !nsGreen;
                setPhase(nsGreen);
                timer = 0;
            }
            // else: wait for next frame until intersection is clear
        }
    }

    private void setPhase(boolean nsGreen) {
        for (TrafficLight tl : northSouth)
            tl.setState(nsGreen ? TrafficLight.State.GREEN : TrafficLight.State.RED);
        for (TrafficLight tl : eastWest)
            tl.setState(nsGreen ? TrafficLight.State.RED : TrafficLight.State.GREEN);
    }
}
