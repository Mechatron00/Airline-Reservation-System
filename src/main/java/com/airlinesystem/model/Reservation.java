package com.airlinesystem.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
@Entity
public class Reservation implements Serializable
{
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int srNo;
    @Id()
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String reservationNumber;
    private String fromLocation;
    private String destination;
    private double price;



    @ManyToOne(targetEntity = Passenger.class, cascade= CascadeType.DETACH)
    @JsonIgnoreProperties({"age", "gender","mobile","reservations","flight"})
    private Passenger passenger;


    @ManyToMany(targetEntity = Flight.class)
    @JsonIgnoreProperties({"price","seatLeft","description","plane","passengers"})
    private List<Flight> flights;



    public Reservation(){}

    public Reservation(String fromLocation, String destination, double price, Passenger passenger, List<Flight> flights ) {
        this.fromLocation = fromLocation;
        this.destination = destination;
        this.price = price;
        this.passenger = passenger;
        this.flights = flights;



    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }



}
