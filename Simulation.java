
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

public class Simulation {
    private Pane root;
    private AnimationTimer timer;
    private long lastTime;

    public Simulation(Pane root) {
        this.root = root;
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
        // future: update cars, lights, etc.
    }
}
