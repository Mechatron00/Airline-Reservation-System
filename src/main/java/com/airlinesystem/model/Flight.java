package com.airlinesystem.model;

import java.util.Date;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Flight 
{
	    @Id
	    private String flightNumber;

	    private String fromLocation;
	    private String destination;
	    private int seatLeft;
	    private String description;
	    private double price;
	    private Date departureTime;
	    private Date arrivalTime;


	    @OneToOne(targetEntity = Plane.class, cascade= CascadeType.ALL)
	    private Plane plane;

	    @ManyToMany(targetEntity =Passenger.class)
	    private List<Passenger> passengers;


	    public Flight(){}

	    public Flight(String flightNumber, String fromLocation, String destination,
                int seatLeft, String description, double price, Date departureTime,
                Date arrivalTime, Plane plane, List<Passenger> passengers)
	    {
	        super();
	        this.flightNumber = flightNumber;
	        this.fromLocation = fromLocation;
	        this.destination = destination;
	        this.seatLeft = seatLeft;
	        this.description = description;
	        this.price = price;
	        this.departureTime = departureTime;
	        this.arrivalTime = arrivalTime;
	        this.plane = plane;
	        this.passengers = passengers;
	    }

	    public String getFlightNumber() {
	        return flightNumber;
	    }

	    public void setFlightNumber(String flightNumber) {
	        this.flightNumber = flightNumber;
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

	    public int getSeatLeft() {
	        return seatLeft;
	    }

	    public void setSeatLeft(int seatLeft) {
	        this.seatLeft = seatLeft;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public double getPrice() {
	        return price;
	    }

	    public void setPrice(double price) {
	        this.price = price;
	    }

	    public Date getDepartureTime() {
	        return departureTime;
	    }

	    public void setDepartureTime(Date departureTime) {
	        this.departureTime = departureTime;
	    }

	    public Date getArrivalTime() {
	        return arrivalTime;
	    }

	    public void setArrivalTime(Date arrivalTime) {
	        this.arrivalTime = arrivalTime;
	    }

	    public Plane getPlane() {
	        return plane;
	    }

	    public void setPlane(Plane plane) {
	        this.plane = plane;
	    }

	    public List<Passenger> getPassengers() {
	        return passengers;
	    }

	    public void setPassengers(List<Passenger> passengers) {
	        this.passengers = passengers;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(flightNumber);
	    }

	    @Override
	    public boolean equals(Object obj)
	    {
	        if(this == obj)
	            return true;
	        if(obj == null)
	            return false;
	        if(getClass() != obj.getClass())
	            return false;
	        Flight other=(Flight)obj;
	        return Objects.equals(flightNumber, other.flightNumber);

	    }
}
