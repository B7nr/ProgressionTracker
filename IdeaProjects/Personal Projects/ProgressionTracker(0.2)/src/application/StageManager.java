package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {

    private static Stage currentStage;

    public StageManager() {}

    public StageManager(Stage stage) { currentStage = stage;}

    public void launchTracker() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(tracker.Controller.class.getResource("tracker.fxml"));
        stage.setTitle("Tracker");
        stage.setScene(new Scene(root, 640, 400));
        currentStage = stage;
        showStage();
    }

    // shows the stage
    // pre: currentStage != null
    public void showStage() {
        if (currentStage == null)
            System.out.println("currentStage is null");
        currentStage.show();
    }

    // changes currentStage to newStage
    public void setCurrentStage(Stage newStage) {
        currentStage = newStage;
    }

    // returns the currentStage
    public Stage getCurrentStage() {
        return currentStage;
    }

    // closes the currentStage
    public void closeStage() {
        currentStage.close();
    }

}
