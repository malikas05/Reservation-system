/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 Course code: 10453
 Stu ID: 101043865
 Malik Iavari
*/
public class Malik_Passenger {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private byte hasImage;
    private ObservableList<Malik_Flight> flights = FXCollections.observableArrayList();

    public Malik_Passenger(String username, String password, String firstName, String lastName, LocalDate age, byte hasImage) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        LocalDate now = LocalDate.now();
        this.age = Period.between(age, now).getYears();
        this.hasImage = hasImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ObservableList<Malik_Flight> getFlights() {
        return flights;
    }

    public void setFlights(ObservableList<Malik_Flight> flights) {
        this.flights = flights;
    }

    public byte getHasImage() {
        return hasImage;
    }

    public void setHasImage(byte hasImage) {
        this.hasImage = hasImage;
    }
    
    
}
