/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/*
 Course code: 10453
 Stu ID: 101043865
 Malik Iavari
*/
public class Malik_Flight {
    private IntegerProperty id;
    private StringProperty number;
    private StringProperty source;
    private StringProperty destination;
    private DoubleProperty flightFare;
    private IntegerProperty distance;
    private ObjectProperty<Date> timeDate;

    public Malik_Flight(int id, String number, String source, String destination, double flightFare, int distance, Date timeDate) {
        this.id = new SimpleIntegerProperty(id);
        this.number = new SimpleStringProperty(number);
        this.source = new SimpleStringProperty(source);
        this.destination = new SimpleStringProperty(destination);
        this.flightFare = new SimpleDoubleProperty(flightFare);
        this.distance = new SimpleIntegerProperty(distance);
        this.timeDate = new SimpleObjectProperty<Date>(timeDate);
    }
    
    public int getId(){
        return id.get();
    }
    
    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }


    public String getNumber(){
        return number.get();
    }
    
    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getSource(){
        return source.get();
    }
    
    public StringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
    }
    
    public String getDestination(){
        return destination.get();
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }
    
    public double getFlightFare(){
        return flightFare.get();
    }

    public DoubleProperty flightFareProperty() {
        return flightFare;
    }

    public void setFlightFare(double flightFare) {
        this.flightFare.set(flightFare);
    }

    public int getDistance(){
        return distance.get();
    }
    
    public IntegerProperty distanceProperty() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance.set(distance);
    }

    public Date getTimeDate(){
        return timeDate.get();
    }
    
    public ObjectProperty<Date> timeDateProperty() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate.set(timeDate);
    }

    
}
