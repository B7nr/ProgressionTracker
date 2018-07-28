package accounts;

import application.StageManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

// controls the Accounts Stage
public class Controller {
    @FXML private Text actionTarget;
    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    private static Stage currentStage;
    private StageManager stageManager;

    // Handles when the login button is pressed
    @FXML protected void handleLoginButton(ActionEvent event) {
        if (stageManager == null) {
           stageManager = new StageManager();
        }
        Authenticator auth = new Authenticator(passwordField, username);
        if (auth.getAuthenticity()) {
            actionTarget.setText("INCORRECT username or password");
            reset();
        } else {
            stageManager.closeStage();
            try {
                stageManager.launchTracker();
            } catch (IOException e) {
                System.out.println("Something went wrong unable to launch Tracker");
            }
        }
    }

    // Handles when the sign up button is pressed
    @FXML protected void handleSignUpButton(ActionEvent event) {
        Accounts acct = new Accounts(passwordField, username);
        if (!acct.addAccount())
            actionTarget.setText("Username already taken.");
        else
            actionTarget.setText("Signed Up!");
        reset();
    }

    private void reset() {
        username.clear();
        passwordField.clear();
    }

//    // Class to manage the account stage
//    public static class AccountManagement extends Application {
//
//        //private Stage currentStage;
//
//        @Override
//        public void start(Stage primaryStage) throws Exception {
//            Parent root = FXMLLoader.load(getClass().getResource("accounts.fxml"));
//            primaryStage.setTitle("Account");
//            primaryStage.setScene(new Scene(root, 300, 250));
//            primaryStage.show();
//            currentStage = primaryStage;
//        }
//
//        private void closeStage() {
//            currentStage.close();
//        }
//
//        private void launchApp(String[] args) {
//            launch(args);
//        }
//
//    }

}
