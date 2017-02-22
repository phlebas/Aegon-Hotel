package uk.co.alpha60.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    Set<Booking> bookings = new HashSet<>();

    private Float rate;

    private Integer occupancy;

    public Room() {

    }

    public Room(Long id, int occupancy, Float rate) {
        this.id = id;
        this.occupancy = occupancy;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }
}
