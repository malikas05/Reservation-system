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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Malik_Database;

public class Malik_UserPageController {

    private Malik_Main mainApp;

    @FXML
    private Label welcomeLabel;
    @FXML
    private ImageView photo;

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
    }

    public void initializeAllComponents() {
        try {
            ResultSet rs = Malik_Database.dbExecuteQuery("select image from user where username = '" + mainApp.getPassenger().getUsername() + "'");
            if (rs.next()) {
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        welcomeLabel.setText("Hello, " + mainApp.getPassenger().getFirstName() + "!");
        if (mainApp.getPassenger().getHasImage() == 0) {
            photo.setImage(new Image("file:resources/images/profile.png"));
        } else {
            photo.setImage(new Image("file:photo.jpg"));
        }
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
}
