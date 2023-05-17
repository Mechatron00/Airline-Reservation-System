package com.airlinesystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinesystem.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,String>
{

    Optional<Passenger> findById(String id);

    Passenger findByUsernameAndPassword(String username, String password);
    Passenger findByMobileAndPassword(String mobile, String password);
//    Passenger findByEmailAndPassword(String email, String password);
}
