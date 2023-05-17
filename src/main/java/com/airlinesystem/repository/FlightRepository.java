package com.airlinesystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinesystem.model.Flight;
@Repository
public interface FlightRepository  extends JpaRepository<Flight, String>
{
    Optional<Flight> getFlightByFlightNumber(String flightnumber);

}
