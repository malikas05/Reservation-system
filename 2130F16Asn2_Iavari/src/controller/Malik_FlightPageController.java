package controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Malik_Flight;
import util.Malik_Database;
import util.Malik_DateUtil;

public class Malik_FlightPageController {

    @FXML
    private TableView<Malik_Flight> flightTable;
    @FXML
    private TableColumn<Malik_Flight, String> sourceColumn;
    @FXML
    private TableColumn<Malik_Flight, String> destinationColumn;

    @FXML
    private Label numberLabel;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label destinationLabel;
    @FXML
    private Label flightFareLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label timeDateLabel;

    private Malik_Main mainApp;

    public Malik_FlightPageController() {
    }

    @FXML
    private void initialize() {
        //Initialization of the table of flights with two columns
        sourceColumn.setCellValueFactory(cellData -> cellData.getValue().sourceProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());

        showFlightDetails(null);

        // Listen to the changes of choice, and if any show
        // info about the flight
        flightTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFlightDetails(newValue));
    }

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;

        // Here we add data to the table from observable list
        flightTable.setItems(mainApp.getFlights());
    }

    @FXML
    private void goBack() {
        mainApp.initUserPage();
    }

    @FXML
    private void bookFlight() {
        Malik_Flight selectedItem = flightTable.getSelectionModel().getSelectedItem();
        String ticketInfo = "";
        ticketInfo += "- Flight Number: " + selectedItem.getNumber();
        ticketInfo += "\n- Source: " + selectedItem.getSource();
        ticketInfo += "\n- Destination: " + selectedItem.getDestination();
        ticketInfo += "\n- Flight Fare: " + selectedItem.getFlightFare() + " + 13% = $"
                + Double.valueOf(new DecimalFormat("#.##").format(selectedItem.getFlightFare() * 1.13));
        ticketInfo += "\n- Distance: " + selectedItem.getDistance() + " km";
        ticketInfo += "\n- Date and time: " + Malik_DateUtil.format(selectedItem.getTimeDate());
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to continue?");
            alert.setContentText(ticketInfo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Malik_Database.dbExecuteUpdate("insert into userFlight (username, flightId) values ('"
                        + mainApp.getPassenger().getUsername()
                        + "', " + selectedItem.getId() + ")");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Malik_FlightPageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Malik_FlightPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showFlightDetails(Malik_Flight flight) {
        if (flight != null) {
            // Fill all labels with the flight info.
            numberLabel.setText(flight.getNumber());
            sourceLabel.setText(flight.getSource());
            destinationLabel.setText(flight.getDestination());
            flightFareLabel.setText("$" + Double.toString(flight.getFlightFare()));
            distanceLabel.setText(Integer.toString(flight.getDistance()) + " km");
            timeDateLabel.setText(Malik_DateUtil.format(flight.getTimeDate()));

        } else {
            // If flight == null, then remove all text
            numberLabel.setText("");
            sourceLabel.setText("");
            destinationLabel.setText("");
            flightFareLabel.setText("");
            distanceLabel.setText("");
            timeDateLabel.setText("");
        }
    }
}
