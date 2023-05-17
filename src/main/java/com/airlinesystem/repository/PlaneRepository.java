package com.airlinesystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airlinesystem.model.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, String>
{
    Optional<Plane> findById(String id);
}