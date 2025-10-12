import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLight {
    public enum State {
        RED, GREEN
    }

    private final Circle shape;
    private State state;

    public TrafficLight(double x, double y, State initialState) {
        this.state = initialState;
        this.shape = new Circle(x, y, 12);
        this.shape.setFill(getColor());
    }

    // No update needed - lights are controlled externally by TrafficController
    public void update(double dt) {}

    private Color getColor() {
        return (state == State.RED) ? Color.RED : Color.LIMEGREEN;
    }

    public Circle getShape() {
        return shape;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        this.state = s;
        this.shape.setFill(getColor());
    }
}
