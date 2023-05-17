package com.airlinesystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinesystem.model.Flight;
import com.airlinesystem.model.Passenger;
import com.airlinesystem.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>
{
    Reservation findByReservationNumber(String reservationNumber);

    List<Reservation> findAllByFlightsIn(List<Flight> flights);

    List<Reservation> findByPassenger(Passenger passenger);
}
