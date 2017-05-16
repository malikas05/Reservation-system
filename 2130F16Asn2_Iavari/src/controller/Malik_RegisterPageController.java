package controller;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Malik_Passenger;
import util.Malik_Database;
import util.Malik_DateUtil;

public class Malik_RegisterPageController {

    private Malik_Main mainApp;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private DatePicker birthdateField;

    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Label firstnameError;
    @FXML
    private Label lastnameError;
    @FXML
    private Label birthdateError;

    @FXML
    private Button registerBtn;

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
        registerBtn.addEventHandler(KeyEvent.KEY_PRESSED, keyListener);
    }

    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER);
            {
                registerOperation();
            }
        }
    };

    @FXML
    private void registerOperation() {
        cleanAllErrors();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String birthdate = Malik_DateUtil.formatBirthDate(birthdateField.getValue());
        if (isInputValid(username, password, firstname, lastname, birthdate)) {
            try {
                Malik_Database.dbExecuteUpdate("insert into user (username, password, firstname, "
                        + "lastname, birthdate) values ('"
                        + username + "', '"
                        + password + "', '"
                        + firstname + "', '"
                        + lastname + "', '"
                        + birthdate + "')");
                mainApp.setPassenger(new Malik_Passenger(username, password,
                        firstname, lastname, Malik_DateUtil.parseBirthDate(birthdate), (byte) 0));
                mainApp.initUserPage();
                cleanAllFields();
            } catch (SQLException ex) {
                usernameError.setText("* This username is already used");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void goBack() {
        cleanAllFields();
        mainApp.backToPage();
    }

    private void cleanAllErrors() {
        usernameError.setText("");
        passwordError.setText("");
        firstnameError.setText("");
        lastnameError.setText("");
        birthdateError.setText("");
    }

    private void cleanAllFields() {
        usernameField.setText("");
        firstnameField.setText("");
        lastnameField.setText("");
        passwordField.setText("");
        birthdateField.setValue(null);
        usernameError.setText("");
        passwordError.setText("");
        firstnameError.setText("");
        lastnameError.setText("");
        birthdateError.setText("");
    }

    private static boolean checkFields(String field, int i) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_-]{3,15}$");
        if (i == 1) {
            p = Pattern.compile("^[A-Za-z]{3,15}$");
        }
        Matcher m = p.matcher(field);
        return m.matches();
    }

    private boolean isInputValid(String username, String password, String firstname, String lastname, String birthdate) {
        boolean errorMessage = false;

        if (username == null || username.length() == 0 || !checkFields(username, 0)) {
            usernameError.setText("* No valid user name");
            errorMessage = true;
        }
        if (password == null || password.length() == 0 || !checkFields(password, 0)) {
            passwordError.setText("* No valid password");
            errorMessage = true;
        }
        if (firstname == null || firstname.length() == 0 || !checkFields(firstname, 1)) {
            firstnameError.setText("* No valid first name");
            errorMessage = true;
        }
        if (lastname == null || lastname.length() == 0 || !checkFields(lastname, 1)) {
            lastnameError.setText("* No valid last name");
            errorMessage = true;
        }
        if (birthdate == null || birthdate.length() == 0 || !Malik_DateUtil.validDate(birthdate)) {
            birthdateError.setText("* No valid birth date");
            errorMessage = true;
        }

        if (!errorMessage) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.showAndWait();

            return false;
        }
    }
}
