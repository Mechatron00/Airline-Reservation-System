package com.airlinesystem.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlinesystem.service.FlightService;
import com.airlinesystem.util.BadRequest;
import com.airlinesystem.util.ExceptionHandle;
import com.airlinesystem.util.Response;

import javassist.NotFoundException;

@RestController
public class FlightController
{
    @Autowired
    private FlightService flightServices;

    @GetMapping("/flight/{flightNumber}")//postman checked
    public ResponseEntity<?> getFlightByNumber(@PathVariable("flightNumber") String flightNumber)
    {
        try
        {
           return flightServices.getFlightByNumber(flightNumber);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new Response(200, "Flight with flight number "+ flightNumber +" has been deleted successfully!"));
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404,exception.getMessage())));
        }

    }


    @DeleteMapping("/airline/{flightNumber}")//postman checked
    public ResponseEntity<?> deleteFlight(@PathVariable("flightNumber") String flightNumber)
    {
       try
       {
           flightServices.deleteFlight(flightNumber);
           return ResponseEntity.status(HttpStatus.OK)
                   .body(new Response(200, "Flight " + flightNumber + " has been deleted successfully."));
       }
       catch (NotFoundException exception)
       {
           return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
       }
       catch (IllegalArgumentException exception)
       {
           return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400,exception.getMessage())));
       }



    }




    /*
    String flightNumber, double price, String fromLocation, String destination,
                                          String departureTime, String arrivalTime, String description, int capacity,
                                          String model, String manufacturer, String planeDescription)
     */
    @PostMapping("/flight/{flightNumber}")//
    public  ResponseEntity<?> updateFlight(@PathVariable("flightNumber") String flightNumber,
                                           @RequestParam("price") double price,@RequestParam("fromLocation") String fromLocation,
                                           @RequestParam("destination") String destination, @RequestParam("departureTime") String departureTime,
                                           @RequestParam("arrivalTime") String arrivalTime, @RequestParam("description") String description,
                                           @RequestParam("capacity") int capacity, @RequestParam("model") String model,
                                           @RequestParam("manufacturer") String manufacturer, @RequestParam("planeDescription") String planeDescription)
    {

        try
        {
            return flightServices.updateFlight(flightNumber, price, fromLocation, destination, departureTime, arrivalTime, description,
                    capacity, model, manufacturer, planeDescription);

        }
        catch (ParseException exception)
        {
            return  ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
        catch (IllegalArgumentException exception)
        {
            return  ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }

    }
}
