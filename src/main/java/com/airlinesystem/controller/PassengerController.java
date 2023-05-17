package com.airlinesystem.controller;

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

import com.airlinesystem.service.PassengerService;
import com.airlinesystem.util.BadRequest;
import com.airlinesystem.util.ExceptionHandle;
import com.airlinesystem.util.Response;

import javassist.NotFoundException;

@RestController
public class PassengerController
{
    @Autowired
    private PassengerService passengerService;

    /*
    * long id, String firstname, String middlename, String lastname, int age, String username,
                                              String email, String password
    * */

    @PutMapping("/passenger/{id}")//postman checked
    public ResponseEntity<?> updatePassenger(@PathVariable("id") String id,
                                             @RequestParam("firstname") String firstname,
                                             @RequestParam("middlename") String middlename,
                                             @RequestParam("lastname") String lastname,
                                             @RequestParam("age") Integer age,
                                             @RequestParam("username") String username,
                                             @RequestParam("email") String email,
                                             @RequestParam("password") String password
    )
    {

        try
        {
            return passengerService.updatePassenger(id,firstname,middlename,lastname,age,username,email,password);
        }
        catch (IllegalArgumentException exception)
        {
            return  ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
        }


    }

    @DeleteMapping("/passenger/{id}")//postman checked
    public ResponseEntity<?> deletePassenger(@PathVariable String id)
    {
        try
        {
            passengerService.deletePassenger(id);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Passenger with id "+ id + " deleted successfully!"));
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
        }
    }

    /*
    *
    * String firstname, String middlename, String lastname, int age,
                                             String gender, String mobile, String username, String email, String password
    * */


    @PostMapping("/passenger")//postman checked
    public ResponseEntity<?> createPassenger(@RequestParam("firstname") String firstname,
                                             @RequestParam("middlename") String middlename,
                                             @RequestParam("lastname") String lastname,
                                             @RequestParam("age") Integer age,
                                             @RequestParam("gender") String gender,
                                             @RequestParam("mobile") String mobile,
                                             @RequestParam("username") String username,
                                             @RequestParam("email") String email,
                                             @RequestParam("password") String password
    )
    {
        try
        {
            return passengerService.createPassenger(firstname,middlename,lastname,age,gender,mobile,username,email,password);
        }
        catch (IllegalArgumentException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, exception.getMessage())));
        }
    }



    @GetMapping("/passenger/{id}")//postman checked
    public ResponseEntity<?> getPassenger(@PathVariable("id") String id)
    {
        try
        {
            return passengerService.getPassenger(id);
        }
        catch (NotFoundException exception)
        {
            return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, exception.getMessage())));
        }
    }
}
