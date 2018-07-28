/*
Class to handle signing up an account
 */

package accounts;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.HashMap;

public class Accounts {

    private static final String USERS_FILE = "users.txt";
    private static final String DATA_FILE = "data.txt";
    private static HashMap<String, Boolean> userMap;
    private final PasswordField PASSWORD;
    private final TextField USER;

    public Accounts(PasswordField passwordField, TextField username) {
        userMap = new HashMap<>();
        getMap();
        PASSWORD = passwordField;
        USER = username;
    }

    // creates the userMap from file of users
    private void getMap() {
        try {
            File temp = new File(USERS_FILE);
            if (temp.createNewFile()) { // attempts to create a file if not available
                System.out.println("cleanup 1 ran");
                File cleanUp = new File(DATA_FILE);
                cleanUp.delete();
                cleanUp.createNewFile();
            }
            BufferedReader file = new BufferedReader(new FileReader(USERS_FILE));
            while (file.ready()) {
                userMap.put(file.readLine(), true);
            }
        } catch (IOException e) {
            System.out.println("Error in users file");
            // TODO MAKE BETTER
        }
    }

    // adds an account to the database
    // returns true if successfully added false otherwise
    public boolean addAccount() {
        System.out.println("addAccount ran");
        if (checkUsername()) {
            System.out.println("checkUsername was true");
            try {
                FileWriter userFile = new FileWriter(new File(USERS_FILE), true);
                userFile.write(USER.getText() + System.lineSeparator());
                userFile.close();
            } catch (IOException e) {
                System.out.println("Issue with UserFile");
                return false;
                // TODO MAKE BETTER
            }
            addAccountHelp();
            return true;
        }
        return false;
    }

    private boolean checkUsername() {
        int oldSize = userMap.size();
        userMap.putIfAbsent(USER.getText(), true);
        return oldSize < userMap.size();
    }

    private void addAccountHelp() {
        System.out.println("addAccountHelp ran");
        FileWriter file;
        try {
            File temp = new File(DATA_FILE);
            if (temp.createNewFile()) { // tries to create a file if not available
                System.out.println("cleanup 2 ran");
                File cleanUp = new File(USERS_FILE);
                cleanUp.delete();
                cleanUp.createNewFile();
            }
            file = new FileWriter(temp, true);
            file.write(USER.getText() + System.lineSeparator());
            file.flush();
            file.write(PASSWORD.hashCode() + System.lineSeparator());
            file.close();
        } catch (IOException e) {
            System.out.println("exception found!!");
            //TODO MAKE BETTER
        }
    }
}
