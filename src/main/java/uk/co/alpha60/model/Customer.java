package uk.co.alpha60.model;

import com.sun.javafx.beans.IDProperty;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "customer")
    Set<Booking> bookings = new HashSet<>();

    public Customer() {}

    public Customer(Long id, String customerName) {
        this.id = id;
        this.customerName = customerName;
    }

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

}
