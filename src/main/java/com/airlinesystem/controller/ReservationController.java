package com.airlinesystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlinesystem.service.ReservationService;
import com.airlinesystem.util.BadRequest;
import com.airlinesystem.util.ExceptionHandle;
import com.airlinesystem.util.Response;

import javassist.NotFoundException;

@RestController
public class ReservationController
{
    @Autowired
    private ReservationService reservationService;


    @GetMapping("/reservation/{number}")//postman checked
    public ResponseEntity<?> getReservation(@PathVariable String number)
    {
        try
        {
            return reservationService.getReservation(number);
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
        }

    }

    /*
     *long passengerId, List<String> flightNumbers
     */
    @PostMapping("/reservation")//postman checked
    public ResponseEntity<?> createReservation(@RequestParam("passengerId") String passengerId,
                                               @RequestParam("flightNumbers") List<String> flightNumbers)
    {

        try
        {
            return reservationService.createReservation(passengerId, flightNumbers);
        }
        catch (IllegalArgumentException exception)
        {
        	return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
    }


    /*
     * String number, List<String> flightsAdded, List<String> flightsRemoved
     * */
    @PostMapping("/reservation/{number}")//postman checked
    public ResponseEntity<?> updateReservation(@PathVariable String number,
                                               @RequestParam(required = false) List<String> flightsAdded,
                                               @RequestParam(required = false) List<String> flightsRemoved)
    {
        try
        {
            return reservationService.updateReservation(number,flightsAdded,flightsRemoved);
        }
        catch (IllegalArgumentException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
    }


    /*
     * String reservationNumber
     * */
    @DeleteMapping("/reservation/{number}")//postman checked
    public ResponseEntity<?> cancelReservation(@PathVariable String number)
    {
        try
        {
            reservationService.cancelReservation(number);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Reservation with number "+ number + " cancelled successfully!"));
        }
        catch (NotFoundException exception)
        {
            return  ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
        }

    }


}
