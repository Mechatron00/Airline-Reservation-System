package com.airlinesystem.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
//@IdClass(PassengerID.class)
public class Passenger implements Serializable
{

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String firstname;
    private String middlename;
    private String lastname;
    private int age;
    private String gender;



    @Column(unique = true)
    private String mobile;

    @Column(unique = true)
    
    private String username;
    
    @Column(unique = true)

    private String email;
    private String password;

    @OneToMany(targetEntity = Reservation.class, cascade=CascadeType.ALL)
    private List<Reservation> reservations;

    public Passenger() {}

    public Passenger(String firstname, String middlename, String lastname, int age,
                     String gender, String mobile, String username, String email, String password
    )
    {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.age = age;
        this.gender = gender;
        this.mobile = mobile;
        this.username = username;
        this.email = email;
        this.password = password;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


}