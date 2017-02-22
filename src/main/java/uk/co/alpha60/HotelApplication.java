package uk.co.alpha60;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.co.alpha60.model.BookingRepository;
import uk.co.alpha60.model.CustomerRepository;
import uk.co.alpha60.model.Room;
import uk.co.alpha60.model.RoomRepository;

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
        return (evt) -> Arrays.asList(1L, 2L, 3L, 4L, 5L).forEach(
                a -> {
                    Room[] rooms = new Room[5];
                    rooms[0] = roomRepository.save(new Room(a, 1, 100.0f));
                    rooms[1] = roomRepository.save(new Room(a, 1, 100.0f));
                    rooms[2] = roomRepository.save(new Room(a, 2, 200.0f));
                    rooms[3] = roomRepository.save(new Room(a, 2, 200.0f));
                    rooms[4] = roomRepository.save(new Room(a, 2, 200.0f));
                });
    }

}


//TODO      A GET message which will retrieve any bookings for a specific room ID

//TODO		A GET message which will retrieve any bookings for a specific customer ID

//TODO		A GET message to determine the availability of a specific room during a given date range

//TODO		A POST message to create a new booking.