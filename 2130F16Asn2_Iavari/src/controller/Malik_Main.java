package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Malik_Flight;
import model.Malik_Passenger;
import util.Malik_Database;
import util.Malik_DateUtil;

public class Malik_Main extends Application {

    private Stage primaryStage;
    private Scene mainPage;
    private Scene loginPage;
    private Scene registerPage;
    private Scene flightPage;

    private Malik_Passenger passenger;
    private ObservableList<Malik_Flight> flights = FXCollections.observableArrayList();

    public Malik_Main() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAllFlightsFromDB();
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Travel System");
        this.primaryStage.getIcons().add(new Image("file:resources/images/img.png"));

        initMainPage();
        initLoginPage();
        initRegisterPage();
        initFlightPage();
    }

    public void getAllFlightsFromDB() {
        try {
            ResultSet rs = Malik_Database.dbExecuteQuery("select * from flight");
            int countFlights = 0;
            while (rs.next()) {
                flights.add(new Malik_Flight(rs.getInt("id"), rs.getString("number"), rs.getString("source"),
                        rs.getString("destination"), rs.getDouble("flightFare"), rs.getInt("distance"), rs.getDate("timeDate")));
                if (flights.get(countFlights).getTimeDate().before(new Date())) {
                    Date dateChanged = Malik_DateUtil.addDays(flights.get(countFlights).getTimeDate());
                    Malik_Database.updateFlightDate(dateChanged, flights.get(countFlights).getId());
                    flights.get(countFlights).setTimeDate(dateChanged);
                }
                countFlights++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_MainPage.fxml"));
            AnchorPane mainAnchor = (AnchorPane) loader.load();

            Malik_MainPageController controller = loader.getController();
            controller.setMainApp(this);

            // Display the primary stage.
            mainPage = new Scene(mainAnchor);
            primaryStage.setScene(mainPage);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initLoginPage() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_LoginPage.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_LoginPageController controller = loader.getController();
            controller.setMainApp(this);

            loginPage = new Scene(loginAnchor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRegisterPage() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_RegisterPage.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_RegisterPageController controller = loader.getController();
            controller.setMainApp(this);

            registerPage = new Scene(loginAnchor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initFlightPage() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_FlightPage.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_FlightPageController controller = loader.getController();
            controller.setMainApp(this);

            flightPage = new Scene(loginAnchor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initUserPage() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_UserPage.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_UserPageController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(loginAnchor));
            controller.initializeAllComponents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initUserPageTickets() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_UserPageTickets.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_UserPageTicketsController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(loginAnchor));
            controller.initializeAllComponents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initUserPageSettings() {
        try {
            // Here we load an fxml file and display the page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Malik_Main.class.getResource("../view/Malik_UserPageSettings.fxml"));
            AnchorPane loginAnchor = (AnchorPane) loader.load();

            Malik_UserPageSettingsController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(loginAnchor));
            controller.initializeAllComponents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToPage() {
        primaryStage.setScene(mainPage);
    }

    //all getters and setters
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getMainPage() {
        return mainPage;
    }

    public Scene getLoginPage() {
        return loginPage;
    }

    public Scene getRegisterPage() {
        return registerPage;
    }

    public Scene getFlightPage() {
        return flightPage;
    }

    public ObservableList<Malik_Flight> getFlights() {
        return flights;
    }

    public Malik_Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Malik_Passenger passenger) {
        this.passenger = passenger;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
