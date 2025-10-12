import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Car {
    private double x, y;
    private double speed;
    private String fromDirection; // The direction the car is coming from
    private String direction; // The current direction of travel
    private Rectangle shape;
    private String route;

    private static final double WIDTH = 40;
    private static final double HEIGHT = 40;

    private enum TurnPhase { NONE, PHASE_1, PHASE_2 }
    private TurnPhase turnPhase = TurnPhase.NONE;

    public Car(double x, double y, String direction, double speed, String route) {
        this.x = x;
        this.y = y;
        this.fromDirection = direction;
        this.direction = direction;
        this.speed = speed;
        this.route = route;

        Color color;
        switch (route) {
            case "LEFT" -> color = Color.RED;
            case "RIGHT" -> color = Color.LIMEGREEN;
            default -> color = Color.BLUE; // STRAIGHT
        }

        shape = new Rectangle(WIDTH, HEIGHT, color);
        shape.setX(x);
        shape.setY(y);
    }

    public void update(double dt) {
        if (turnPhase == TurnPhase.NONE && !route.equals("STRAIGHT") && isInIntersection()) {
            turnPhase = TurnPhase.PHASE_1;
        }

        if (turnPhase != TurnPhase.NONE) {
            if (route.equals("LEFT")) {
                performLeftTurn(dt);
            } else { // RIGHT
                performRightTurn(dt);
            }
        } else {
            moveStraight(dt);
        }

        shape.setX(x);
        shape.setY(y);
    }

    private void performLeftTurn(double dt) {
        if (turnPhase == TurnPhase.PHASE_1) {
            boolean targetReached = false;
            switch (fromDirection) {
                case "N": // From South
                    if (y > 354) { y -= speed * dt; } else { y = 354; targetReached = true; }
                    break;
                case "S": // From North
                    if (y < 406) { y += speed * dt; } else { y = 406; targetReached = true; }
                    break;
                case "E": // From West
                    if (x < 406) { x += speed * dt; } else { x = 406; targetReached = true; }
                    break;
                case "W": // From East
                    if (x > 354) { x -= speed * dt; } else { x = 354; targetReached = true; }
                    break;
            }
            if (targetReached) {
                direction = turnLeftDirection(fromDirection);
                turnPhase = TurnPhase.PHASE_2;
            }
        } else if (turnPhase == TurnPhase.PHASE_2) {
            moveStraight(dt);
        }
    }

    private void performRightTurn(double dt) {
        if (turnPhase == TurnPhase.PHASE_1) {
            boolean targetReached = false;
            switch (fromDirection) {
                case "N": // From South, turning East
                    if (y > 406) { y -= speed * dt; } else { y = 406; targetReached = true; }
                    break;
                case "S": // From North, turning West
                    if (y < 354) { y += speed * dt; } else { y = 354; targetReached = true; }
                    break;
                case "E": // From West, turning South
                    if (x > 354) { x -= speed * dt; } else { x = 354; targetReached = true; }
                    break;
                case "W": // From East, turning North
                    if (x < 406) { x += speed * dt; } else { x = 406; targetReached = true; }
                    break;
            }
            if (targetReached) {
                direction = turnRightDirection(fromDirection);
                turnPhase = TurnPhase.PHASE_2;
            }
        } else if (turnPhase == TurnPhase.PHASE_2) {
            moveStraight(dt);
        }
    }

    private void moveStraight(double dt) {
        switch (direction) {
            case "N" -> y -= speed * dt;
            case "S" -> y += speed * dt;
            case "E" -> x += speed * dt;
            case "W" -> x -= speed * dt;
        }
    }

    public boolean isInIntersection() {
        // Check if the car is anywhere within the main intersection area
        return x > 350 && x < 450 && y > 350 && y < 450;
    }

    private String turnLeftDirection(String dir) {
        return switch (dir) {
            case "N" -> "W";
            case "S" -> "E";
            case "E" -> "N";
            case "W" -> "S";
            default -> dir;
        };
    }

    private String turnRightDirection(String dir) {
        return switch (dir) {
            case "N" -> "E";
            case "S" -> "W";
            case "E" -> "S";
            case "W" -> "N";
            default -> dir;
        };
    }

    public Rectangle getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }

    public String getFromDirection() {
        return fromDirection;
    }

    public boolean isOffScreen(double windowSize) {
        return x < -50 || x > windowSize + 50 || y < -50 || y > windowSize + 50;
    }
}