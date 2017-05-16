package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Malik_Flight;
import util.Malik_Database;
import util.Malik_DateUtil;

public class Malik_UserPageTicketsController {

    private Malik_Main mainApp;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Malik_Flight> ticketsTable;
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

    private ObservableList<Malik_Flight> flights = FXCollections.observableArrayList();

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
        try {
            ResultSet rs = Malik_Database.dbExecuteQuery("select f.id, f.number, f.source, f.destination, "
                    + "f.flightFare, f.distance, f.timeDate from flight f inner join userFlight uF on "
                    + "f.id = uF.flightId inner join user u on uF.username = u.username where u.username = '"
                    + mainApp.getPassenger().getUsername() + "'");
            while (rs.next()) {
                flights.add(new Malik_Flight(rs.getInt("id"), rs.getString("number"), rs.getString("source"),
                        rs.getString("destination"), Double.valueOf(new DecimalFormat("#.##").format(rs.getDouble("flightFare") * 1.13)),
                        rs.getInt("distance"), rs.getDate("timeDate")));
            }
            ticketsTable.setItems(flights);
        } catch (SQLException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initializeAllComponents() {
        welcomeLabel.setText("Hello, " + mainApp.getPassenger().getFirstName() + "!");
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
    private void displayTicketInfo() {
        Malik_Flight selectedItem = ticketsTable.getSelectionModel().getSelectedItem();
        String ticketInfo = "";
        ticketInfo += String.format("- Passenger: %s %s", mainApp.getPassenger().getFirstName(),
                mainApp.getPassenger().getLastName());
        ticketInfo += String.format("\n- Age: %d", mainApp.getPassenger().getAge());
        ticketInfo += "\n- Flight Number: " + selectedItem.getNumber();
        ticketInfo += "\n- Source: " + selectedItem.getSource();
        ticketInfo += "\n- Destination: " + selectedItem.getDestination();
        ticketInfo += "\n- Flight Fare: " + selectedItem.getFlightFare() + " + 13% = $"
                + Double.valueOf(new DecimalFormat("#.##").format(selectedItem.getFlightFare() * 1.13));
        ticketInfo += "\n- Distance: " + selectedItem.getDistance() + " km";
        ticketInfo += "\n- Date and time: " + Malik_DateUtil.format(selectedItem.getTimeDate());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Ticket Info");
        alert.setHeaderText("You ticket information is provided below.");
        alert.setContentText(ticketInfo);
        alert.showAndWait();
    }

    @FXML
    private void deleteTicketOperation() {
        try {
            int flightId = ticketsTable.getSelectionModel().getSelectedItem().getId();
            if (flightId > 0) {
                Malik_Database.dbExecuteUpdate("delete from userFlight where username = '"
                        + mainApp.getPassenger().getUsername() + "' and flightId = " + flightId
                        + " limit 1");
                deleteTicketFromList(flightId);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Ticket Info");
                alert.setHeaderText("Please choose a ticket from the table.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Malik_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deleteTicketFromList(int id) {
        for (Malik_Flight flight : flights) {
            if (flight.getId() == id) {
                flights.remove(flight);
                break;
            }
        }
    }

    @FXML
    private void initialize() {
        //Initialization of the table of flights with two columns
        sourceColumn.setCellValueFactory(cellData -> cellData.getValue().sourceProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());

        showFlightDetails(null);

        // Listen to the changes of choice, and if any show
        // info about the flight
        ticketsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFlightDetails(newValue));
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
