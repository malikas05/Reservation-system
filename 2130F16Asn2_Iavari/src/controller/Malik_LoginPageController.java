package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Malik_Passenger;
import util.Malik_Database;
import util.Malik_DateUtil;

public class Malik_LoginPageController {

    private Malik_Main mainApp;

    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label errorMsg;
    @FXML
    private Button loginBtn;

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
        loginBtn.addEventHandler(KeyEvent.KEY_PRESSED, keyListener);
    }

    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER);
            {
                loginOperation();
            }
        }
    };

    @FXML
    public void loginOperation() {
        String username = userNameField.getText().trim();
        String password = passwordField.getText().trim();
        if (isInputValid(username, password)) {
            try {
                ResultSet rs = Malik_Database.dbExecuteQuery("select * from user where username = '" + username + "' and password = '"
                        + password + "'");
                if (rs.next()) {
                    mainApp.setPassenger(new Malik_Passenger(rs.getString("username"), rs.getString("password"),
                            rs.getString("firstname"), rs.getString("lastname"), Malik_DateUtil.parseBirthDate(rs.getString("birthdate")), (byte) 0));
                    InputStream image = rs.getBinaryStream("image");
                    if (image != null) {
                        try (OutputStream out = new FileOutputStream(new File("photo.jpg"));) {
                            while (image.available() > 0) {
                                out.write(image.read());
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Malik_LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                image.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Malik_LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        mainApp.getPassenger().setHasImage((byte) 1);
                    }

                    mainApp.initUserPage();
                    cleanAllFields();
                } else {
                    errorMsg.setText("* Login or Password is incorrect.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static boolean checkFields(String field, int i) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_-]{3,15}$");
        if (i == 1) {
            p = Pattern.compile("^[A-Za-z]{3,15}$");
        }
        Matcher m = p.matcher(field);
        return m.matches();
    }

    private boolean isInputValid(String username, String password) {
        boolean errorMessage = false;

        if (username == null || username.length() == 0 || !checkFields(username, 0)) {
            errorMessage = true;
        }
        if (password == null || password.length() == 0 || !checkFields(password, 0)) {
            errorMessage = true;
        }

        if (!errorMessage) {
            return true;
        } else {
            errorMsg.setText("* Login or Password is incorrect.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.showAndWait();
            return false;
        }
    }

    private void cleanAllFields() {
        errorMsg.setText("");
        userNameField.setText("");
        passwordField.setText("");
    }

    @FXML
    private void goToRegisterPage() {
        mainApp.getPrimaryStage().setScene(mainApp.getRegisterPage());
    }

    @FXML
    private void goBack() {
        cleanAllFields();
        mainApp.backToPage();
    }
}
