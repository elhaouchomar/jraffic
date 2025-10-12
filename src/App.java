
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

// The Simulation class in your JavaFX application serves as the core game loop.
// It manages the continuous updating of the simulation's state, ensuring that
// the application remains responsive and that animations proceed smoothly.

public class App extends Application {

    private Pane root;
    private Simulation simulation;

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(800, 800);

        Scene scene = new Scene(root);
        stage.setTitle("Jraffic Simulation");
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> simulation.addCar("UP");
                case DOWN -> simulation.addCar("DOWN");
                case LEFT -> simulation.addCar("LEFT");
                case RIGHT -> simulation.addCar("RIGHT");
                case R -> simulation.addCarRandom(); // random direction
                case ESCAPE -> javafx.application.Platform.exit();
}
        });

        simulation = new Simulation(root);
        simulation.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
