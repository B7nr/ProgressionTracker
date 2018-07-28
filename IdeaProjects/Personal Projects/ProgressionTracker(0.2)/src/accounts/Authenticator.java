/*
Class to handle login authentication
 */

package accounts;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Authenticator {
    private final boolean AUTHENTICITY;
    // constructor that takes in the password and username and verifies
    // if it exists in the database or not
    public Authenticator(PasswordField password, TextField userName) {
        AUTHENTICITY = verify(password, userName);
    }

    // TODO make more secure!!!! hash????
    private boolean verify(PasswordField password, TextField userName) {
        FileReader pws;
        try {
            pws = new FileReader("data.txt");
        } catch (IOException e) {
            // no users have profiles
            return false;
        }
        BufferedReader pwList = new BufferedReader(pws);
        try {
            while (pwList.ready()) {
                String actual = pwList.readLine() + pwList.readLine(); // reads username then password
                if ((userName.getText() + password.hashCode()).equals(actual)) {
                    pwList.close();
                    return true;
                }
            }
        } catch (IOException e) {
            //TODO MAKE BETTER
            System.out.println("An IOException occurred in AUTHENTICATOR");
            return false;
        }
        return false;
    }

    // returns whether the password and username
    // exists
    // returns true if does false otherwise
    public boolean getAuthenticity() {
        return AUTHENTICITY;
    }
}
