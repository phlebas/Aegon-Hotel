package uk.co.alpha60.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @JsonIgnore
    @ManyToOne
    private Customer customer;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Room room;

    private LocalDate checkIn;
    private LocalDate checkOut;

    public Booking() {

    }

    public Booking(Long id, Room room, Customer customer, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.room = room;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
