package com.airlinesystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.airlinesystem.model.Flight;
import com.airlinesystem.model.Passenger;
import com.airlinesystem.model.Plane;
import com.airlinesystem.model.Reservation;
import com.airlinesystem.repository.FlightRepository;
import com.airlinesystem.repository.PassengerRepository;
import com.airlinesystem.repository.ReservationRepository;

import javassist.NotFoundException;

@Service
public class FlightService
{
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    public ResponseEntity<?> getFlightByNumber(String flightNumber) throws NotFoundException
    {
        Optional<Flight> res=flightRepository.getFlightByFlightNumber(flightNumber);
        if(res.isPresent())
        {
            Flight flight=res.get();
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }
        else
        {
            throw new NotFoundException("Sorry, the requested flight with number " + flightNumber + " does not exist");
        }
    }

    public ResponseEntity<?> updateFlight(String flightNumber, double price,  String fromLocation, String destination,
                                          String departureTime, String arrivalTime, String description, int capacity,
                                          String model, String manufacturer, String planeDescription) throws ParseException
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH");
        Date dTime = formatter.parse(departureTime);
        Date aTime = formatter.parse(arrivalTime);
        if(fromLocation.equals(destination) || aTime.compareTo(dTime) <= 0)
        {
            throw new IllegalArgumentException("Illegal argument entered to create/update flight!");
        }
        Optional<Flight> res = flightRepository.getFlightByFlightNumber(flightNumber);
        Flight flight;
        Plane plane;
        if(res.isPresent())
        {
            flight = res.get();
            Flight finalFlight = flight;
            List<Reservation> reservationList = reservationRepository.findAllByFlightsIn(new ArrayList<Flight>(){{add(finalFlight);}});

            if(reservationList.size() > capacity)
            {
                throw new IllegalArgumentException("target capacity is less than active reservations");
            }
            if(!checkValidUpdate(flight, aTime, dTime))
            {
                throw new IllegalArgumentException("flight timings overlapped!");

            }
            flight.setPrice(price);
            flight.setFromLocation(fromLocation);
            flight.setDestination(destination);
            flight.setDepartureTime(dTime);
            flight.setArrivalTime(aTime);
            flight.setDescription(description);
            flight.setSeatLeft(capacity);
            flight.getPlane().setCapacity(capacity);
            flight.getPlane().setModel(model);
            flight.getPlane().setManufacturer(manufacturer);
            flight.getPlane().setDescription(planeDescription);
        }
        else
        {
            plane = new Plane(model, capacity, manufacturer, planeDescription);
            flight = new Flight(flightNumber, fromLocation, destination,capacity,description,price, dTime, aTime,  plane,new ArrayList<>());
        }

        flight = flightRepository.save(flight);
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }


    public void deleteFlight(String flightNumber) throws NotFoundException
    {
        Optional<Flight> res = flightRepository.getFlightByFlightNumber(flightNumber);
        if (res.isPresent())
        {
            Flight flight=res.get();
            List<Reservation> reservationList = reservationRepository.findAllByFlightsIn(new ArrayList<Flight>(){{add(flight);}});
            if (!reservationList.isEmpty())
            {
                throw new IllegalArgumentException("Flight"+ flightNumber +" has active reservations");
            }
            else {
                flightRepository.delete(flight);
                new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else
        {
            throw new NotFoundException("Sorry, the requested flight with number " + flightNumber + " does not exist");
        }
    }



    private boolean checkValidUpdate(Flight currentFlight, Date currentFlightArrivalTime, Date currentFlightDepartureTime)
    {
        for(Passenger passenger : passengerRepository.findAll())
        {
            Set<Flight> flights=new HashSet<>();
            for(Reservation reservation : passenger.getReservations())
            {
                flights.addAll(reservation.getFlights());
            }
            if(flights.contains(currentFlight))
            {
                flights.remove(currentFlight);
                for(Flight flight : flights)
                {
                    Date flightDepartureTime = flight.getDepartureTime();
                    Date flightArrivalTime = flight.getArrivalTime();
                    if (currentFlightArrivalTime.compareTo(flightDepartureTime) >=0
                            && currentFlightDepartureTime.compareTo(flightArrivalTime) <=0)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}