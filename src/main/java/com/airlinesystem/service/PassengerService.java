package com.airlinesystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.airlinesystem.model.Flight;
import com.airlinesystem.model.Passenger;
import com.airlinesystem.model.Reservation;
import com.airlinesystem.repository.PassengerRepository;
import com.airlinesystem.repository.ReservationRepository;

import javassist.NotFoundException;

@Service
public class PassengerService
{
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public ResponseEntity<?> createPassenger(String firstname, String middlename, String lastname, int age,
                                             String gender, String mobile, String username, String email, String password)
    {
        Passenger isPassengerExist = passengerRepository.findByUsernameAndPassword(username,password);
        if(isPassengerExist == null)
        {
            Passenger newPassenger = new Passenger(firstname,middlename,lastname,age,gender,mobile,username,email,password);
            Passenger res = passengerRepository.save(newPassenger);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else
        {
            throw new IllegalArgumentException("Another passenger is already registered with same username");
        }
    }


    public  ResponseEntity<?> updatePassenger(String id, String firstname, String middlename, String lastname, int age, String username,
                                              String email, String password)throws NotFoundException
    {
        Optional<Passenger> existingPass = passengerRepository.findById(id);
        if(existingPass.isPresent()) {
            try {
                Passenger isPassengerExist = passengerRepository.findByUsernameAndPassword(username, password);
                if (isPassengerExist == null) {
                    Passenger passenger = existingPass.get();
                    passenger.setFirstname(firstname);
                    passenger.setMiddlename(middlename);
                    passenger.setLastname(lastname);
                    passenger.setAge(age);
                    passenger.setEmail(email);

                    Passenger res = passengerRepository.save(passenger);
                    return new ResponseEntity<>(res, HttpStatus.OK);

                } else {
                    throw new IllegalArgumentException("Passenger with same username already exist");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Passenger with " + id + " does not exist!");
            }
        }
        else
        {
            throw new NotFoundException("Passenger with "+id+" Does not exist");
        }
    }

    public void deleteReservation(Reservation reservation, Passenger passenger)
    {
        try
        {
            for(Flight flight : reservation.getFlights())
            {
                updateFlightSeats(flight);
                flight.getPassengers().remove(passenger);
            }
            passenger.getReservations().remove(reservation);
            reservationRepository.delete(reservation);
        }
        catch (Exception e)
        {
            System.out.println("Error ocuured!");
        }
    }

    private void updateFlightSeats(Flight flight)
    {
        try
        {
            flight.setSeatLeft(flight.getSeatLeft()+1);
        }
        catch (Exception e)
        {
            System.out.println("Error occured!");
        }
    }


    public ResponseEntity<?> deletePassenger(String id) throws NotFoundException
    {
        Optional<Passenger> existingPass = passengerRepository.findById(id);
        if(existingPass.isPresent())
        {
            List<Reservation> reservations = reservationRepository.findByPassenger(existingPass.get());
            System.out.println("Reservation:"+reservations.size());

            for (Reservation reservation : reservations)
            {
                System.out.println("Reservation:"+reservation.getPassenger());
                deleteReservation(reservation, existingPass.get());

            }
            passengerRepository.deleteById(id);
            return new ResponseEntity<>("Passenger with id "+ id +" deleted successfully", HttpStatus.OK);
        }
        else
        {
           throw new NotFoundException("Passenger with id "+id+" does not exist");
        }
    }


    public ResponseEntity<?> getPassenger(String id) throws NotFoundException
    {
        Optional<Passenger> existingPass = passengerRepository.findById(id);
        if(existingPass.isPresent())
        {
            Passenger passenger = existingPass.get();
            return new ResponseEntity<>(passenger, HttpStatus.OK);
        }
        else
        {
            throw new NotFoundException("Sorry, the requested passenger with ID "+id+" does not exist");
        }
    }
}