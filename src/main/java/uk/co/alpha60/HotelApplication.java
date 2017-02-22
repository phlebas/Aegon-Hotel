package uk.co.alpha60;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.co.alpha60.model.*;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class HotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BookingRepository bookingRepository,
                           CustomerRepository customerRepository,
                           RoomRepository roomRepository) {
        Customer[] customers = new Customer[3];
        customers[0] = new Customer(1L, "customer1");
        customers[1] = new Customer(2L, "customer2");
        customers[2] = new Customer(3L, "customer3");

        Arrays.asList(customers).forEach(
                customer -> {
                    customerRepository.save(customer);
                }
        );

        Room[] rooms = new Room[5];
        rooms[0] = new Room(1L, 1, 100.0f);
        rooms[1] = new Room(2L, 1, 100.0f);
        rooms[2] = new Room(3L, 2, 200.0f);
        rooms[3] = new Room(4L, 2, 200.0f);
        rooms[4] = new Room(5L, 2, 200.0f);
        Arrays.asList(rooms).forEach(
                room -> {
                    roomRepository.save(room);
                });

        Booking[] bookings = new Booking[3];
        LocalDate checkin = LocalDate.now().plusDays(3);
        bookings[0] = new Booking(1L, rooms[0], customers[0], checkin, checkin.plusDays(4));
        bookings[1] = new Booking(2L, rooms[2], customers[2], checkin.plusDays(1), checkin.plusDays(2));
        bookings[2] = new Booking(3L, rooms[2], customers[1], checkin.plusDays(4), checkin.plusDays(5));

        return (evt) -> Arrays.asList(bookings).forEach(
                booking -> {
                    bookingRepository.save(booking);
                }
        );
    }

}


//TODO      A GET message which will retrieve any bookings for a specific room ID

//TODO		A GET message which will retrieve any bookings for a specific customer ID

//TODO		A GET message to determine the availability of a specific room during a given date range

//TODO		A POST message to create a new booking.