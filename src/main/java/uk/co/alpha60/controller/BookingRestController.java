package uk.co.alpha60.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import uk.co.alpha60.model.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingRestController {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingRestController(BookingRepository bookingRepository,
                                 CustomerRepository customerRepository,
                                 RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Retrieves any bookings for a specific room ID
     * @param roomId the room Id
     * @return a list of bookings
     */
    @RequestMapping(path="/room/{roomId}", method = RequestMethod.GET)
    Collection<Booking> getBookingsForRoom(@PathVariable Long roomId) {
        validateRoom(roomId);
        return bookingRepository.findByRoomId(roomId);
    }

    /**
     * Retrieves any bookings for a specific customer ID
     * @param customerId the customer Id
     * @return a list of bookings
     */
    @RequestMapping(path = "/customer/{customerId}", method = RequestMethod.GET)
    Collection<Booking> getBookingsForCustomer(@PathVariable Long customerId) {
        validateCustomerId(customerId);
        return bookingRepository.findByCustomerId(customerId);
    }

    /**
     * Determines the availability of a specific room during a given date range
     * @param roomId the room Id
     * @param checkIn checkin date
     * @param checkOut checkout date
     * @return True iff a the room is available between these dates.
     */
    @RequestMapping(path = "/available/{roomId}/{checkIn}/{checkOut}", method = RequestMethod.GET)
    public Boolean isRoomAvailable(@PathVariable Long roomId,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        validateRoom(roomId);
        validateDates(checkIn, checkOut);
        return roomHasNoBookings(roomId, checkIn, checkOut);
    }

    private Boolean roomHasNoBookings(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> bookings = bookingRepository.getBookings(roomId, checkIn, checkOut);
        return bookings.isEmpty();
    }

    /**
     * Makes a booking for a room.
     * @param noOfGuests the number of guests. Can be either 1 or 2
     * @param customerId the cutomer's Id
     * @param checkIn the desired check in date
     * @param checkOut the desired check out date
     * @return the Booking object created.
     */
    @RequestMapping(path="/book", method= RequestMethod.POST)
    public Booking makeBooking(@RequestParam Integer noOfGuests, @RequestParam Long customerId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        validateDates(checkIn, checkOut);
        validateNoOfGuests(noOfGuests);
        Room room = findRoom(noOfGuests, checkIn, checkOut);
        Customer customer = validateCustomerId(customerId);
        return bookingRepository.save(new Booking(null, room, customer, checkIn, checkOut));

    }

    private Room findRoom(Integer noOfGuests, LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms = roomRepository.findByOccupancy(noOfGuests);
        for (Room room : rooms) {
            if (roomHasNoBookings(room.getId(), checkIn, checkOut)) {
                return room;
            }
        }
        throw new NoAvailableRoomExecption();
    }

    private void validateNoOfGuests(Integer noOfGuests) {
        if (noOfGuests < 1 || noOfGuests > 2) {
            throw new InvalidOccupancyException(noOfGuests);
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new InvalidBookingDatesException("Checkout date cannot be before checkin date.");
        }
    }

    private void validateRoom(Long roomId) throws RoomNotFoundException {
        roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException(roomId));
    }

    private Customer validateCustomerId(Long customerId) throws RoomNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

}
