import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    private Simulation simulation;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(800, 800);

        Scene scene = new Scene(root);
        stage.setTitle("Jraffic Simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> simulation.addCar("UP");
                case DOWN -> simulation.addCar("DOWN");
                case LEFT -> simulation.addCar("LEFT");
                case RIGHT -> simulation.addCar("RIGHT");
                case R -> simulation.addCarRandom();
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