package com.airlinesystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.airlinesystem.model.Flight;
import com.airlinesystem.model.Passenger;
import com.airlinesystem.model.Reservation;
import com.airlinesystem.repository.FlightRepository;
import com.airlinesystem.repository.PassengerRepository;
import com.airlinesystem.repository.ReservationRepository;

import javassist.NotFoundException;

import javax.transaction.Transactional;

@Service
public class ReservationService
{
    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;



    /*
     *
     *
     * getting reservation by reservation id
     *
     */
    public ResponseEntity<?> getReservation(String id) throws NotFoundException
    {
        Optional<Reservation> existingRes = reservationRepository.findById(id);
        if(existingRes.isPresent())
        {
            Reservation reservation = existingRes.get();
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        }
        else
        {
            throw new NotFoundException("Reservation with number " + id + " does not exist");
        }

    }
    /*
     *
     *
     * creating new reservation
     *
     *
     */
    public ResponseEntity<?> createReservation(String passengerId, List<String> flightNumbers)
    {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(passenger.isPresent() && !CollectionUtils.isEmpty(flightNumbers))
        {
            List<String> trimmedFlightNumbers = flightNumbers.stream().map(String :: trim).collect(Collectors.toList());
                   

            System.out.println("flight numbers: "+ trimmedFlightNumbers);
            List<Flight> flightList = getFlightList(trimmedFlightNumbers);
            if (flightList.size() > 1)
            {
                boolean isFirstTimeOverlap = isTimeOverlapWithinReservation(flightList);
                if(isFirstTimeOverlap)
                {
                    System.out.println("Time overlap within same reservation!");
                    throw new IllegalArgumentException("Cannot create Reservation, there is time overlap between the flights within same reservation");
                }
                boolean isSecondTimeOverlap = isTimeOverlapForSamePerson(passengerId, flightList);
                if(isSecondTimeOverlap)
                {
                    System.out.println("Time overlap within for same person");
                    throw new IllegalArgumentException("Cannot create reservation, the same person cannot have two reservations that overlap with each other");
                }

            }
            //checking capacity
            if(isSeatsAvailable(flightList))
            {
                //fare price
                double fare = calculatePrice(flightList);

                //making new reservation

                Reservation newReservation = new Reservation(flightList.get(0).getFromLocation(), flightList.get(flightList.size()-1).getDestination(), fare,passenger.get(),flightList);

                //adding passenger
                passenger.get().getReservations().add(newReservation);

                //adding passenger to the flight
                for (Flight flight : flightList)
                {
                    flight.getPassengers().add(passenger.get());
                }

                reduceAvailableFlightSeats(flightList);
                Reservation res = reservationRepository.save(newReservation);
                return new ResponseEntity<>(res, HttpStatus.OK);

            }
            else
            {
                throw new IllegalArgumentException("can not create reservation, no seats available!");
            }

        }
        else
            throw  new IllegalArgumentException("can not create reservation, Passenger or Flight number must not empty.");

    }
    /*
     *
     *
     * updating reservation
     *
     */
    public ResponseEntity<?> updateReservation(String number, List<String> flightsAdded, List<String> flightsRemoved) throws NotFoundException
    {
        Reservation existingReservation = reservationRepository.findByReservationNumber(number);
        if(existingReservation != null)
        {
            List<Flight> existingFlightList = existingReservation.getFlights();
            if(CollectionUtils.isEmpty(flightsAdded) && !CollectionUtils.isEmpty(flightsRemoved) && existingFlightList.size() <= flightsRemoved.size())
            {
                throw new IllegalArgumentException("can not update, Reservation has lesser or equal flights user trying to remove!");

            }
            Passenger passenger = existingReservation.getPassenger();
            String passengerId = passenger.getId();

            double existingPrice = existingReservation.getPrice();

            //removing  flights

            if(!CollectionUtils.isEmpty(flightsRemoved))
            {
                System.out.println("flight removed:"+flightsRemoved);
                List<String> trimmedFlightsRemoved = flightsRemoved.stream()
                        .map(String::trim)
                        .collect(Collectors.toList());
                List<Flight> flightListToRemoved = getFlightList(trimmedFlightsRemoved);
                if(existingFlightList.size() != 0)
                {
                    existingFlightList.removeAll(flightListToRemoved);
                    increaseAvailableFlightSeats(flightListToRemoved);
                }
                double newPrice = existingPrice-calculatePrice(flightListToRemoved);
                if(existingFlightList.size() > 0)
                {
                    existingReservation.setFromLocation(existingFlightList.get(0).getFromLocation());
                    existingReservation.setDestination(existingFlightList.get(existingFlightList.size()-1).getDestination());
                }
                existingReservation.setPrice(newPrice);
            }
            ////Adding flights
            if(CollectionUtils.isEmpty(flightsAdded))
            {
                System.out.println("flights added:"+flightsAdded);
                List<String> trimmedFlightsAdded = flightsAdded.stream()
                        .map(String::trim)
                        .collect(Collectors.toList());

                List<Flight> flightListToAdd = getFlightList(trimmedFlightsAdded);
                if(flightListToAdd.size() > 1)
                {
                    boolean isFirstTimeOverlap = isTimeOverlapWithinReservation(flightListToAdd);
                    if(isFirstTimeOverlap)
                    {
                        System.out.println("Time overlap within same reservation!");
                        throw new IllegalArgumentException("cannot update, time overlapping!");
                    }
                    boolean isSecondTimeOverlap = isTimeOverlapForSamePerson(passengerId, flightListToAdd);
                    if(isSecondTimeOverlap)
                    {
                        System.out.println("Time overlap within for same person!");
                        throw  new IllegalArgumentException("can not update, the same person cannot have two reservations at same time!");
                    }
                }
                if(isSeatsAvailable(flightListToAdd))
                {
                    double newPrice = existingPrice+calculatePrice(flightListToAdd);
                    existingFlightList.addAll(flightListToAdd);
                    existingReservation.setFlights(existingFlightList);
                    existingReservation.setFromLocation(existingFlightList.get(0).getFromLocation());
                    existingReservation.setDestination(existingFlightList.get(existingFlightList.size() -1).getDestination());
                    existingReservation.setPrice(newPrice);
                    reduceAvailableFlightSeats(flightListToAdd);
                }
                else
                {
                    throw new IllegalArgumentException("No seats available. Flight capacity full!");
                }
            }

            Reservation resUpdate= reservationRepository.save(existingReservation);

            return new ResponseEntity<>(resUpdate, HttpStatus.OK);
        }
        else
        {
            throw new NotFoundException("No reservation found for given reservation number ");
        }
    }

    @Transactional
    public ResponseEntity<?> cancelReservation(String reservationNumber) throws NotFoundException
    {
        Reservation res = reservationRepository.findByReservationNumber(reservationNumber);
        if (res != null)
        {
            res.getPassenger().getReservations().remove(res);
            reservationRepository.delete(res);
            List<Flight> flightList = res.getFlights();
            if(flightList.size() != 0)
                increaseAvailableFlightSeats(flightList);
            return new ResponseEntity<>("Reservation with number "+reservationNumber+" canceled successfully!",HttpStatus.OK);

        }
        else
        {
            throw new NotFoundException("Reservation with number "+reservationNumber+" does not exist");
        }

    }

    private void increaseAvailableFlightSeats(List<Flight> flightList)
    {
        for (Flight flight : flightList)
        {
            flight.setSeatLeft(flight.getSeatLeft() - 1);
        }
    }


    private void reduceAvailableFlightSeats(List<Flight> flightList)
    {
        for (Flight flight : flightList)
        {
            flight.setSeatLeft(flight.getSeatLeft() - 1);
        }
    }

    private double calculatePrice(List<Flight> flightList)
    {
        double price = 0;
        for (Flight f : flightList)
        {
            price += f.getPrice();
        }
        return price;

    }

    private boolean isSeatsAvailable(List<Flight> flightList)
    {
        for(Flight flight : flightList)
        {
            if(flight.getSeatLeft() <= 0)return false;
        }
        return true;
    }


    private boolean isTimeOverlapForSamePerson(String passengerId, List<Flight> flightList)
    {
        //reservations for that passenger
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        List<Reservation> reservationList = passenger.get().getReservations();

        //current reservation
        List<Flight> currentFlightList = new ArrayList<Flight>();
        for (Reservation res : reservationList)
        {
            for (Flight flight : res.getFlights())
            {
                currentFlightList.add(flight);
            }
        }
        System.out.println("existing Reservations for passenger with id " +passengerId +" is :"+ currentFlightList);

        //internal overlapping
        for(int i=0;i< flightList.size();i++)
        {
            for(int j=i+1;j< currentFlightList.size();j++)
            {
                Date startDate1 = flightList.get(i).getDepartureTime();
                Date endDate1 = flightList.get(i).getArrivalTime();

                Date startDate2 = currentFlightList.get(j).getDepartureTime();
                Date endDate2 = currentFlightList.get(j).getArrivalTime();
                if(startDate1.compareTo(endDate2) <= 0 && endDate1.compareTo(startDate2) >=0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isTimeOverlapWithinReservation(List<Flight> flightList)
    {
        for (int i=0;i<flightList.size();i++)
        {
            for(int j=i+1;j<flightList.size();j++)
            {
                Date startDate1 = flightList.get(i).getDepartureTime();
                Date endDate1 = flightList.get(i).getArrivalTime();

                Date startDate2 = flightList.get(j).getDepartureTime();
                Date endDate2 = flightList.get(j).getArrivalTime();
                if(startDate1.compareTo(endDate2) <= 0 && endDate1.compareTo(startDate2) >= 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Flight> getFlightList(List<String> flightNumbers)
    {
        List<Flight> flightList=new ArrayList<>();
        for(String flightnumber : flightNumbers)
        {
            Optional<Flight> flight = flightRepository.getFlightByFlightNumber(flightnumber);
            if(flight.isPresent())
            {
                flightList.add(flight.get());
            }
        }
        return flightList;
    }
}