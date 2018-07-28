package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(accounts.Controller.class.getResource("accounts.fxml"));
        primaryStage.setTitle("Account");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        new StageManager(primaryStage);
    }

}
