package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import util.Malik_Database;

public class Malik_UserPageSettingsController {

    private Malik_Main mainApp;
    private InputStream image;

    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
    }

    public void initializeAllComponents() {
        welcomeLabel.setText("Hello, " + mainApp.getPassenger().getFirstName() + "!");
        passwordField.setText(mainApp.getPassenger().getPassword());
        firstnameField.setText(mainApp.getPassenger().getFirstName());
        lastnameField.setText(mainApp.getPassenger().getLastName());
    }

    @FXML
    private void logoffOperation() {
        mainApp.getPrimaryStage().setScene(mainApp.getLoginPage());
        mainApp.setPassenger(null);
    }

    @FXML
    private void myPageOperataion() {
        mainApp.initUserPage();
    }

    @FXML
    private void bookFlightOperataion() {
        mainApp.getPrimaryStage().setScene(mainApp.getFlightPage());
    }

    @FXML
    private void displayTicketsOperataion() {
        mainApp.initUserPageTickets();
    }

    @FXML
    private void displaySettingsOperataion() {
        mainApp.initUserPageSettings();
    }

    @FXML
    private void uploadImageOperation() {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterJPEG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            if (file != null) {
                image = new FileInputStream(file);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Malik_UserPageSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void submitOperation() {
        cleanAllErrors();
        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String password = passwordField.getText().trim();
        if (isInputValid(firstname, lastname, password)) {
            try {
                Malik_Database.updateUserSettings(password, firstname, lastname, image, mainApp.getPassenger().getUsername());
                mainApp.getPassenger().setPassword(password);
                mainApp.getPassenger().setFirstName(firstname);
                mainApp.getPassenger().setLastName(lastname);

                image = null;
            } catch (SQLException ex) {
                Logger.getLogger(Malik_UserPageSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Malik_UserPageSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Information");
            alert.setHeaderText("You successfully updated your info.");
            alert.showAndWait();
            mainApp.initUserPage();
        }
    }

    private void cleanAllErrors() {
        firstnameField.setStyle("-fx-background-color: white;");
        lastnameField.setStyle("-fx-background-color: white;");
        passwordField.setStyle("-fx-background-color: white;");
    }

    private boolean checkFields(String field, int i) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_-]{3,15}$");
        if (i == 1) {
            p = Pattern.compile("^[A-Za-z]{3,15}$");
        }
        Matcher m = p.matcher(field);
        return m.matches();
    }

    private boolean isInputValid(String firstname, String lastname, String password) {
        boolean errorMessage = false;

        if (firstname == null || firstname.length() == 0 || !checkFields(firstname, 1)) {
            firstnameField.setStyle("-fx-background-color: #ce1e47;");
            errorMessage = true;
        }
        if (lastname == null || lastname.length() == 0 || !checkFields(lastname, 1)) {
            lastnameField.setStyle("-fx-background-color: #ce1e47;");
            errorMessage = true;
        }
        if (password == null || password.length() == 0 || !checkFields(password, 0)) {
            passwordField.setStyle("-fx-background-color: #ce1e47;");
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
